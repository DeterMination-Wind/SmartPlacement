# 术语表

按主题归类的 Smart Placement 术语，每条说明它在本模组语境下的准确含义。

## 功能与规则

### 物品运输线（item line）
`Conveyor`、`StackConveyor`、`Duct` 及其子类方块的统称；装甲变体（`ArmoredConveyor`、armored `Duct`）靠类型继承自动纳入。

### 交叉替换（crossing replacement）
画线时预测到所放运输线与既有运输线垂直相交，自动改放空配置反向分类器（`invertedSorter`），省去先放 junction 再拆换的返工。

### 垂直判定
`Mathf.mod(existingRotation - planRotation, 2) == 1`，即既有方块朝向与画线方向相差 90°；平行（差 0 或 2）不触发替换。

### 混合科技图（mixed-tech map）
规则界面中行星选"任意"（内部为 `Planets.sun`）的地图，多种科技组共享建造空间。默认只在这类地图启用（`smartplacement-mixed-only`），关掉开关后具体科技行星也生效。

### 线路族（line kind）
`LineKind.conveyor` 为传送带类（`Conveyor` / `StackConveyor`），`LineKind.duct` 为管道类（`Duct`，含装甲管道）。2×2 交叉开关按「线路族 × 线路族」生效，而不是按具体方块。

### 宽阻挡带（wide blocked band）
管道线连续压到的垂直交叉格达到 3 格及以上（超过阈值 2）的区段。这类区段改用物品管道桥整段跳过；不触发分类器三连上限时，1~2 格仍放反向分类器。

### 分类器三连上限（sorter chain limit）
原版 `Sorter.getTileTarget` 在源与目标都是 `instantTransfer`（反向分类器）时返回空，禁止 3 个反向分类器连续串联。因此传送带×管道交叉若会在被跨线轴向形成第 3 个连续分类器，新线就把整段连续交叉带改用本科技的桥跨过，已有两个分类器保持可用。

## 实现机制

### PlacementProxy
`SmartPlacementMod` 的内部类，继承 `Block`，复制源方块的放置属性并委托其行为。只在画线期间顶替 `input.block`，且经 `content.remove()` 排除在内容表外，玩家不可直接选中。

### 惰性代理表（lazy proxy table）
`ensureProxies()` 在 `init()` 与每帧 `Trigger.update` 里尝试建表；内容尚未加载（`Vars.content.blocks()` 为空）时直接返回，下次再试。Neon 在内容加载前调用子模组 `init()`，靠这条语义才不会静默失效。

### 原版回退（vanilla fallback）
不满足替换条件时，把 `req.block` 临时还原为源方块再调 `source.getReplacement()` 得到的原版结果（junction/bridge）。

### 前后计划格（hasFront / hasBack）
沿画线方向，当前格前方与后方是否都存在本批次 `BuildPlan`；两者齐全才可能触发交叉替换，避免在线端点误替换。

## 构建与发布

### 通用 JAR（universal JAR）
同时含桌面 Java 类与安卓 `classes.dex` 的单文件产物，由 `deploy` 任务合成。

### verifyRules
main() 断言式回归任务（JavaExec 运行 `SmartPlacementRulesTest`），挂在 `check` 上；标准 `test` 任务被禁用。

### bekBundled / Neon 聚合
Neon 聚合模组要求子模组主类携带的静态标记与设置接线方法（`bekBundled` / `bekBuildSettings`）。本仓库源码自带这两者并在 `submods.json` 里标了 `injectBekHooks: false`，同步脚本只做镜像拷贝。

