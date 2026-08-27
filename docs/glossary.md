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
规则界面中行星选"任意"（内部为 `Planets.sun`）的地图，多种科技组共享建造空间。替换功能只在这类地图启用，具体科技行星一律原版。

## 实现机制

### PlacementProxy
`SmartPlacementMod` 的内部类，继承 `Block`，复制源方块的放置属性并委托其行为。只在画线期间顶替 `input.block`，且经 `content.remove()` 排除在内容表外，玩家不可直接选中。

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
Neon 聚合模组要求子模组主类携带的静态标记与设置接线方法。本仓库真源不含它们，由 Neon 同步脚本拷贝时注入；本模组无设置项，契约零负担。
