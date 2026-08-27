# 测试指南

自动化只有一个 main() 断言式回归任务，其余靠手测。

## 自动化任务

| 任务 | 主类 | 覆盖内容 |
| --- | --- | --- |
| `verifyRules` | `smartplacement.SmartPlacementRulesTest` | 交叉替换规则与启用条件 |

- `verifyRules` 经 `check.dependsOn` 挂在 `check` 链上；标准 `test` 任务被显式 `enabled = false`，不要恢复它，也不要把 `verifyRules` 从 `check` 摘掉。
- 覆盖：5×5 运输线方块（传送带/装甲传送带/塑钢传送带/导管/装甲导管）交叉矩阵全部返回 `invertedSorter`；平行线、线端点、非运输线方块、单科技（`dualTech=false`）四种情况回退原版；`replacementEnabled` 对 `sun`/非 `sun` 行星的判定。

```powershell
.\gradlew.bat verifyRules
```

改动 `crossingReplacement` / `replacementEnabled` / `getReplacement` 时必须先跑；发版前 `clean build` 会顺带执行。

## 手测清单

发版或改动放置行为前，至少覆盖：

1. **混合科技图**（规则行星设为 `sun`/"任意"）
   - 导管、传送带、塑钢传送带两两垂直交叉：自动放置空配置反向分类器，无需手动拆换。
   - 装甲导管 / 装甲传送带 / 堆叠传送带变体同样生效。
2. **原版回退**：同向平行画线、线的起止端点、交叉处为非运输线方块时，行为与原版一致（junction/bridge）。
3. **单科技图**：规则行星为具体科技行星时，任何交叉都保持原版逻辑。
4. **放置 UI**：不画线时选中方块的图标与名称为源方块（proxy 不外露）；画线开始/结束后 `input.block` 正确切换与还原。
5. **双端输入**：桌面（DesktopInput）与安卓（MobileInput）各画一遍线，线起点无残留偏移。
