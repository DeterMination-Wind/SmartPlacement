# 架构总览

改 `src/` 前先读本文。Smart Placement 只有一个源文件（`smartplacement.SmartPlacementMod.java`），全部机制围绕"画线期间的临时替身"展开。

## 入口与生命周期

- `mod.json` 的 `main` 指向 `smartplacement.SmartPlacementMod`；`minGameVersion 157`、`hidden: true`（不出现在模组列表）、`java: true`。
- 覆写 `init()`，做三件事：
  1. 调用 `ensureProxies()` 尝试建代理表（独立安装时内容已加载，立即建成）；
  2. `Events.run(Trigger.update, this::updatePlacementBlock)` 挂每帧更新；
  3. `Events.on(ClientLoadEvent, ...)` 注册独立设置页（`bekBundled` 时跳过）。
- `ensureProxies()` 必须保持"内容为空就下次再试"的惰性语义：Neon 在**构造期**调用子模组 `init()`，那时 `Vars.content.blocks()` 还是空表；只有靠 `Trigger.update` 里的重试才能在内容加载后补建代理。修改这段逻辑前先读下面「Neon 聚合形态」。

## 核心类与数据流

- **PlacementProxy**（内部类，继承 `Block`）：复制源方块的放置属性（size、offset、rotate、conveyorPlacement、allowDiagonal、group、requirements、category、region 等），`canReplace` / `changePlacementPath` / `handlePlacementLine` 全部委托源方块。
- **updatePlacementBlock()**（Trigger.update）：玩家正在画线（`!input.linePlans.isEmpty()`）、替换启用且原版 `blockreplace` 开着时，把 `input.block` 换成 proxy 并重置 `DesktopInput`/`MobileInput` 的 `lastLineX/lastLineY` 让线起点重算；否则换回源方块。
- **getReplacement()**：先把 `req.block` 临时还原为源方块调 `source.getReplacement()` 拿原版回退（junction/bridge）；若替换启用、画线在本格前后都有计划格、且本格已有建筑，则交 `crossingReplacement()` 判定。
- **crossingReplacement()**（static、可测）：仅当 双科技启用 && 前后都有计划格 && 既有方块朝向与画线方向垂直（`Mathf.mod(existingRotation - planRotation, 2) == 1`）&& 双方都是物品运输线方块 && 对应 2×2 开关开着时，返回 `Blocks.invertedSorter`（空配置反向分类器）；否则返回原版回退。
- **kindOf()**（static、可测）：把方块归到 `LineKind.conveyor`（`Conveyor` / `StackConveyor`）或 `LineKind.duct`（`Duct`，含装甲管道）；非物品线返回 `null`。
- **handleDuctBridgePass()**：原版 `Placement.smartCalculateBridges` 的 `placeable` 是 `(可放置 || 同方块) && !(既有方块垂直且属于 avoid)`，交叉格必然落在第二段的否定里 → 计划被打上“不可放置”。此时 `Duct.handlePlacementLine` 传 `hasJunction=false`（原版 `Duct.init()` 里 junction 赋值被注释掉了），DP 会跳过该计划并架桥盖过去；传送带因为 `junctionReplacement` 非空、`hasJunction=true`，计划被当 junction 保留。所以新线是管道时交叉会退化成管道桥。修法：本批次真的含交叉替换计划时，按传送带的形态调用 `Placement.calculateBridges(plans, ductBridge, true, avoid)`。
- **restoreCrossings()**：架桥会重建计划表，可能把交叉格还原成普通线方块；这里对存活的计划再判一次交叉（桥接计划是桥方块，`kindOf` 为 null，不会被误改）。
- **applyWideDuctBridge()**（`PlacementProxy.handlePlacementLine`）：最后处理“宽阻挡带”——管道线连续压到 3 格及以上的垂直交叉格时，把整段阻挡跳过，用 `Blocks.ductBridge` 连两端并移除中间计划格；跨距超过桥的 `range` 时保持原样（不硬架桥）。`wideBlockedRuns()` 是这段判定的纯函数部分，供离线回归。

## 设置项

| 键 | 默认 | 作用 |
| --- | --- | --- |
| `smartplacement-cross-cc` | 开 | 传送带类 × 传送带类交叉改放反向分类器 |
| `smartplacement-cross-cd` | 开 | 传送带类 × 管道类 |
| `smartplacement-cross-dc` | 开 | 管道类 × 传送带类 |
| `smartplacement-cross-dd` | 开 | 管道类 × 管道类 |
| `smartplacement-duct-bridge-wide` | 开 | 管道交叉阻挡带超过 2 格时改放物品管道桥 |
| `smartplacement-mixed-only` | 开 | 只在行星为“任意”（混合科技）时生效 |

- 文案键是 `setting.<键>.name` / `setting.<键>.description`，见 `src/main/resources/bundles/`。
- `replacementEnabled(sunPlanet, mixedOnly)` 是纯函数：`mixedOnly` 关掉后所有星球都生效。
- 原版 `blockreplace`（设置里的“建筑替换”）关掉时本模组完全不介入——否则会把未注册的 proxy 当作可放置方块。

## Neon 聚合形态

- 本仓库是 Neon 聚合模组的子模组真源（Neon `tools/submods.json` 中 id=`smp`，`localPath` 指向本目录），自身可独立运行。
- 源码自带 `bekBundled` 与 `bekBuildSettings(SettingsMenuDialog.SettingsTable)`；`submods.json` 里该模块 `injectBekHooks: false`，契约由本仓库维护，同步脚本只做镜像拷贝。
- Neon 在 `BekToolsMod` 构造器里 `new SmartPlacementMod()` 后立刻 `init()`，**早于内容加载**。因此：
  - 代理表只能在 `Trigger.update` 里惰性建成（见上）；
  - `bekBundled` 为 true 时不要注册独立设置页，设置由 Neon 统一设置页调用 `bekBuildSettings()` 渲染。
- 改动本仓库后需回 Neon 执行 `python .\tools\update_submods.py --only smp`；不要在 Neon 里手改 `smartplacement` 包。

## 目录速查

```text
src/main/java/smartplacement/SmartPlacementMod.java      全部逻辑：入口、PlacementProxy、替换规则、设置
src/main/resources/bundles/                              设置文案（英文 + zh_CN，同步进 Neon 合并 bundle）
src/test/java/smartplacement/SmartPlacementRulesTest.java  规则回归（main() 断言）
dist/                                                    本地产物（gitignore）：SmartPlacement.jar / .zip
build.gradle                                             任务链：d8InputJar → dexAndroid → deploy → releaseZip → stageRelease
```

## 设计约束（务必保持）

- 纯客户端，不新增可放置方块；proxy 必须留在内容表之外、不画线时必须还原 `input.block`。
- 无歧义场景（平行线、线端点、单科技地图、非运输线交叉）必须原样走原版回退，不干扰普通建造。
- 判定逻辑保持 static 纯函数（`crossingReplacement` / `replacementEnabled` / `kindOf` / `wideBlockedRuns`），供离线回归测试。
- 代理表构建必须容忍"内容尚未加载"（返回后下次再试），不能在 `init()` 里一次性建完就放弃。
