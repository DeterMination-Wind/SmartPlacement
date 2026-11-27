# Smart Placement v1.1.1

## English

### Added

- Seven settings: a 2×2 crossing matrix, duct bridge over wide crossings, bridge instead of a third sorter, and only on mixed-tech planets.
- Duct × duct crossings, including armored ducts, now place the unconfigured inverted sorter as well.
- A duct line crossing a band of 3 or more item lines jumps the whole band with an item duct bridge.
- Inverted sorters only chain two deep, so a conveyor × duct crossing that would add a third keeps the existing two and bridges the new line over the band with its own technology's bridge.

### Fixed

- The bundled Neon build no longer does nothing: the proxy table is built lazily after content loads.
- Duct lines crossing another line no longer degrade into a plain duct bridge; the sorter crossing is kept.

## 中文

### 本次新增

- 七个设置项：2×2 交叉开关、「宽阻挡带改用管道桥」、「三连反向分类器改用桥」、「只在任意星球生效」。
- 物品管道交物品管道（含装甲管道）也会改放未配置反向分类器。
- 管道线撞上 3 格及以上并排交叉阻挡带时，用物品管道桥整段跳过。
- 反向分类器最多连续串两个；传送带×管道交叉会形成第 3 个时，保留已有两个，新线改用本科技的桥跨过交叉带。

### 本次修复

- Neon 聚合版不再完全不生效：代理表改为内容加载后惰性构建。
- 管道线交叉不再退化成普通管道桥，仍保留反向分类器。
