# Smart Placement 文档

Smart Placement（智能放置）的分类文档。文档以中文为主，功能名保留英文本名，便于与代码对照。

## 从哪里开始

| 你是 | 从这里开始 |
| --- | --- |
| 玩家 | [README](../README.md) 的安装与功能说明 |
| 想了解替换规则怎么实现的人 | [架构总览](architecture.md) |
| 想改代码 / 补测试 | [开发指南](development.md) 与[测试指南](testing.md) |
| 准备发版 | [版本与发布](release.md) |
| 遇到不认识的词 | [术语表](glossary.md) |

## 文档地图

```text
docs/
|-- README.md         本页：文档导航
|-- architecture.md   架构总览：入口、PlacementProxy、替换判定、Neon 聚合形态
|-- development.md    开发指南：环境、构建命令、代码风格
|-- release.md        版本与发布：版本号、构建产物链、资产安全
|-- testing.md        测试指南：verifyRules 回归任务、手测清单
`-- glossary.md       术语表
```

## 相关文件

- [README.md](../README.md) / [README_zh.md](../README_zh.md)：项目主页（英文+中文）。
- [AGENTS.md](../AGENTS.md)：仓库维护约束（构建命令、代码约束、Neon 聚合关系）。
- [release_notes_v1.1.0.md](../release_notes_v1.1.0.md) / [release_notes_v1.0.0.md](../release_notes_v1.0.0.md)：版本说明。

## 维护约定

- 面向玩家的行为说明集中在 README；`docs/` 写给想读代码、改代码的人。
- 替换规则一旦调整，必须同步更新 [testing.md](testing.md) 的清单与 `SmartPlacementRulesTest` 的断言。
