# 开发指南

环境、日常命令与代码要点。仓库级约束见 [AGENTS.md](../AGENTS.md)。

## 环境

- 源码与字节码目标 Java 8（`sourceCompatibility 1.8`、`options.release.set(8)`），用较新的 JDK 运行 Gradle 即可。
- 编译期依赖工作区本地目录（无 JitPack 回退，缺目录直接编译失败）：
  - `../Mindustry-master/core/build/classes/java/main`
  - `../Arc/arc-core/build/classes/java/main`
  - `../Arc/backends/backend-android/libs/android.jar`（仅 `dexAndroid` 的 d8 classpath 用）
- D8 探测顺序：`D8_PATH` 环境变量 → `ANDROID_SDK_ROOT`/`ANDROID_HOME` 的 `build-tools` → 工作区根的 `commandlinetools-win-*` 目录；都找不到时 `dexAndroid` 报错。

## 常用命令

```powershell
.\gradlew.bat clean build     # 编译 + verifyRules + 出 dist/ 产物 + 暂存到 ../构建/SmartPlacement/
.\gradlew.bat verifyRules     # 只跑规则回归
.\gradlew.bat deploy          # 只出 dist/SmartPlacement.jar（含 classes.dex）
.\gradlew.bat clean           # 清 build/、dist/ 与暂存目录
```

`build` 依赖 `stageRelease`（自动把产物复制到 `../构建/SmartPlacement/`），任务链细节见 [release.md](release.md)。注意标准 `test` 任务被显式禁用，回归走 `verifyRules`。

## 代码风格要点

- 单文件、`final` 类、Arc 风格大括号换行；判定逻辑写成 static 纯函数以便离线测试。
- 运行时不得引入 Mindustry/Arc 之外的依赖（构建里也只有 `compileOnly` 类路径）。
- 设置项统一用 `Core.settings.getBool(key, def)` 读、`SettingsTable.checkPref(key, def)` 写；文案键 `setting.<key>.name` / `.description` 落在 `src/main/resources/bundles/bundle.properties` 与 `bundle_zh_CN.properties`（中英同步）。
- `bekBundled` / `bekBuildSettings` 契约由本仓库自己维护（Neon `submods.json` 里 `injectBekHooks: false`），不要依赖同步脚本注入。

## 调试建议

- 在地图规则界面把行星设为 `sun`（显示"任意"）即可在本地复现替换行为；换成具体行星应完全原版（或关掉 `smartplacement-mixed-only`）。
- proxy 只在画线期间顶替 `input.block`；若放置 UI 出现替身或图标异常，优先检查 `updatePlacementBlock()` 的换回分支。
- 只在 Neon 里失效、独立安装正常时，先确认 `Vars.content.blocks()` 在 `init()` 是否为空——Neon 在内容加载前调用子模组 `init()`，代理表必须靠 `ensureProxies()` 惰性补建。
