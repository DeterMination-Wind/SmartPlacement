# 测试指南

自动化只有一个 main() 断言式回归任务，其余靠手测。

## 自动化任务

| 任务 | 主类 | 覆盖内容 |
| --- | --- | --- |
| `verifyRules` | `smartplacement.SmartPlacementRulesTest` | 交叉替换矩阵、2×2 开关、行星判定、宽阻挡带分段 |

- `verifyRules` 经 `check.dependsOn` 挂在 `check` 链上；标准 `test` 任务被显式 `enabled = false`，不要恢复它，也不要把 `verifyRules` 从 `check` 摘掉。
- 覆盖：
  - 5×5 运输线方块（传送带/装甲传送带/塑钢传送带/物品管道/装甲管道）交叉矩阵全部返回 `invertedSorter`；
  - 2×2 开关逐格关闭时只影响对应方向，其它格保持开启；
  - 平行线、线端点、非运输线方块、单科技（`dualTech=false`）四种情况回退原版；
  - `replacementEnabled(sunPlanet, mixedPlanetOnly)` 的四种组合；
  - `kindOf` 归类（管道类/传送带类/非物品线）；
  - `wideBlockedRuns` 的阈值语义（超过 2 格才成段、2 格及以下不成段、空表安全）。

```powershell
.\gradlew.bat verifyRules
```

改动 `crossingReplacement` / `replacementEnabled` / `kindOf` / `wideBlockedRuns` / `getReplacement` 时必须先跑；发版前 `clean build` 会顺带执行。

## 手测清单

发版或改动放置行为前，至少覆盖：

1. **混合科技图**（规则行星设为 `sun`/"任意"）
   - 传送带、塑钢传送带、物品管道、装甲管道两两垂直交叉：自动放置空配置反向分类器，无需手动拆换。
   - 关掉某一格开关后，对应方向的交叉回到原版行为，其它方向不受影响。
   - 管道线横穿 3 条及以上并排管道：整段用物品管道桥跳过，对方线路不被逐格覆盖；横穿 1~2 条时仍放分类器。
2. **原版回退**：同向平行画线、线的起止端点、交叉处为非运输线方块时，行为与原版一致（junction/bridge）。
3. **单科技图**：规则行星为具体科技行星时保持原版逻辑；关掉「只在任意星球生效」后规则在具体行星也生效。
4. **放置 UI**：不画线时选中方块的图标与名称为源方块（proxy 不外露）；画线开始/结束后 `input.block` 正确切换与还原。
5. **原版“建筑替换”关闭**：设置里关掉 blockreplace 后本模组完全不介入，不会把替身当方块放出去。
6. **双端输入**：桌面（DesktopInput）与安卓（MobileInput）各画一遍线，线起点无残留偏移。
7. **Neon 聚合**：只装 Neon 时同样生效（模块 `smp` 未被关闭、设置页能看到 6 个开关）。
