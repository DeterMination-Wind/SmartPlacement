# Smart Placement v1.1.0

## English

- Fixed: the bundled Neon build did nothing. Neon initializes bundled modules before the game loads its content, so the proxy table was built from an empty block list. The table is now built lazily on the first frame where content exists, which also keeps standalone behavior unchanged.
- Fixed: a duct line crossing another line degraded into a plain duct bridge. The vanilla bridge DP marks every perpendicular crossing tile as unplaceable, and duct lines run it with `hasJunction = false` (Duct's junction replacement is commented out), so it bridged over the crossing plan instead of keeping the sorter. Conveyor lines pass `hasJunction = true` and kept theirs, which is why only conveyor sources worked. Duct lines now run the same pass the conveyor path uses whenever the batch contains crossings.
- New: six settings — a 2×2 crossing matrix (conveyor × conveyor, conveyor × duct, duct × conveyor, duct × duct), "duct bridge over wide crossings", and "only on mixed-tech planets".
- New: duct × duct (and armored duct) crossings now place the unconfigured inverted sorter as well.
- New: when a duct line runs into a band of 3 or more crossing item lines, it now jumps the whole band with an item duct bridge instead of overwriting it tile by tile. Bands of 1–2 tiles still use sorters, and spans longer than the bridge range are left alone.
- Safety: the mod no longer swaps the selected block while vanilla "block replacement" is disabled, so the internal proxy can never be placed as a real block.

## 中文

- 修复：Neon 聚合版完全不生效。Neon 在游戏加载内容之前就调用子模组 `init()`，那时方块表还是空的，代理表建成空表。现在改为在内容就绪后的第一帧惰性建表，独立安装行为不变。
- 修复：新线是物品管道时，交叉会退化成原版管道桥。原版架桥 DP 会把交叉格判定为“不可放置”，管道没有 junction 替换（`hasJunction=false`）于是直接架桥盖过分类器计划；传送带靠 `hasJunction=true` 保留计划，所以只有“传送带交管道”正常。现在管道线在有交叉替换时按传送带形态跑同一套架桥逻辑。
- 新增 6 个设置项：2×2 交叉开关（传送带×传送带、传送带×管道、管道×传送带、管道×管道）、「宽阻挡带改用管道桥」、「只在任意星球生效」。
- 新增：物品管道交物品管道（含装甲管道）现在也会改放未配置反向分类器。
- 新增：管道线撞上 3 格及以上的并排交叉阻挡带时，用物品管道桥整段跳过，不再逐格覆盖对方管道；1~2 格仍放分类器，超过桥跨距的区间保持原样。
- 安全性：原版「建筑替换」关闭时本模组不再顶替选中方块，内部替身不会被当作真实方块放出去。
