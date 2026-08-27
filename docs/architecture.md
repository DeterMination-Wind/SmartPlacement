# 架构总览

改 `src/` 前先读本文。Smart Placement 只有一个源文件（`smartplacement.SmartPlacementMod.java`，约 170 行），全部机制围绕"画线期间的临时替身"展开。

## 入口与生命周期

- `mod.json` 的 `main` 指向 `smartplacement.SmartPlacementMod`；`minGameVersion 157`、`hidden: true`（不出现在模组列表）、`java: true`。
- 仅覆写 `init()`，做三件事：
  1. 从 `Vars.content.blocks()` 选出所有物品运输线方块（`Conveyor` / `StackConveyor` / `Duct` 实例，装甲变体靠类型继承自动包含）；
  2. 为每个方块建一个 `PlacementProxy` 并 `Vars.content.remove(proxy)` 移出内容表，避免替身出现在可放置内容里；
  3. `Events.run(Trigger.update, this::updatePlacementBlock)` 挂每帧更新。

## 核心类与数据流

- **PlacementProxy**（内部类，继承 `Block`）：复制源方块的放置属性（size、offset、rotate、conveyorPlacement、allowDiagonal、group、requirements、category、region 等），`canReplace` / `changePlacementPath` / `handlePlacementLine` 全部委托源方块。
- **updatePlacementBlock()**（Trigger.update）：玩家正在画线（`!input.linePlans.isEmpty()`）且替换启用时，把 `input.block` 换成 proxy 并重置 `DesktopInput`/`MobileInput` 的 `lastLineX/lastLineY` 让线起点重算；否则换回源方块。
- **getReplacement()**：先把 `req.block` 临时还原为源方块调 `source.getReplacement()` 拿原版回退（junction/bridge）；若替换启用、画线在本格前后都有计划格、且本格已有建筑，则交 `crossingReplacement()` 判定。
- **crossingReplacement()**（static、可测）：仅当 双科技启用 && 前后都有计划格 && 既有方块朝向与画线方向垂直（`Mathf.mod(existingRotation - planRotation, 2) == 1`）&& 双方都是物品运输线方块时，返回 `Blocks.invertedSorter`（空配置反向分类器）；否则返回原版回退。

## 启用条件（无设置系统）

本模组没有设置项、没有 bundle 文案、不注册 `ui.settings.addCategory`。唯一开关是地图规则：`Vars.state.rules.planet == Planets.sun`（规则界面显示为"任意 / Any"的混合科技图）时启用，其余行星一律保持原版行为。

## Neon 聚合形态

- 本仓库是 Neon 聚合模组的子模组真源（Neon `tools/submods.json` 中 id=`smp`，`localPath` 指向本目录），自身可独立运行。
- 当前源码不含 `bekBundled` / `bekBuildSettings`——聚合钩子由 Neon 侧同步脚本（`tools/update_submods.py`）拷贝源码时注入；本模组无设置项，契约对它零负担。
- 改动本仓库后需回 Neon 执行同步；不要在 Neon 里手改 `smartplacement` 包。

## 目录速查

```text
src/main/java/smartplacement/SmartPlacementMod.java      全部逻辑：入口、PlacementProxy、替换规则
src/test/java/smartplacement/SmartPlacementRulesTest.java  规则回归（main() 断言）
dist/                                                    本地产物（gitignore）：SmartPlacement.jar / .zip
build.gradle                                             任务链：d8InputJar → dexAndroid → deploy → releaseZip → stageRelease
```

## 设计约束（务必保持）

- 纯客户端，不新增可放置方块；proxy 必须留在内容表之外、不画线时必须还原 `input.block`。
- 无歧义场景（平行线、线端点、单科技地图、非运输线交叉）必须原样走原版回退，不干扰普通建造。
- 判定逻辑保持 static 纯函数（`crossingReplacement` / `replacementEnabled`），供离线回归测试。
