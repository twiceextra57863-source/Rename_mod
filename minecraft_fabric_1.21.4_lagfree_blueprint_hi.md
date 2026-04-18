# Minecraft Fabric 1.21.4 "Lag-Free" Mod Blueprint (Hindi)

## 1) Pehle reality check (important)
- **Fabric mods Java/Kotlin me bante hain, direct pure C++ me nahi.**
- Agar tum C++ use karna chahte ho, to best approach hai: **Hybrid mod**
  - Fabric side: Java
  - Heavy optimization/native routines: C++ via JNI (optional)
- **"Sabhi phones me chale"** + **"3x FPS guaranteed"** + **"100+ future lag fixes"** practical duniya me guarantee nahi hoti.
  - Different GPU drivers, thermal throttling, RAM limits, Android versions, launcher behavior (Pojav, Fold, etc.) alag hote hain.

## 2) Target definition (achievable)
Is mod ka realistic goal:
1. Frame-time spikes kam karna (stutter reduction)
2. CPU tick pressure kam karna
3. Render workload adaptive banana
4. Network/combat situation me client-side hitching reduce karna
5. Auto-fallback system jisse weak devices pe safe settings apply ho jaye

## 3) Tech stack (Fabric 1.21.4)
- Java 21
- Fabric Loader + Fabric API (Minecraft 1.21.4 compatible versions)
- Mixin (core behavior patching)
- Optional: JNI bridge for C++ routines
- Config: Cloth Config / AutoConfig style pattern

## 4) High-level module architecture

### A) `perf-core`
- Tick profiler hooks
- Real-time MSPT/FPS/frame-time monitor
- Hotspot detection (entity spikes, particle spikes, chunk upload spikes)

### B) `perf-render`
- Dynamic quality scaler
- Adaptive render distance controller
- Particle budget limiter
- Cloud/shadow/translucency auto downgrade under load

### C) `perf-entity`
- Nearby entity update throttling (safe client-side only)
- Animation interpolation budget
- Non-critical entity visual culling

### D) `perf-chunk`
- Chunk rebuild queue prioritization
- Camera-facing chunk preference
- Upload burst smoothing (avoid one-frame heavy uploads)

### E) `perf-pvp`
- Combat-mode profile
- Particle and hit effect budget cap
- Optional HUD simplification in battle mode

### F) `perf-jni` (optional)
- SIMD math kernels (frustum tests, vector transforms)
- Fast spatial filters
- Must include pure-Java fallback if native load fails

## 5) Lag types aur fix strategy

### 5.1 CPU Tick Lag
- Per-tick time budget monitor
- Expensive client tasks split across ticks
- Priority scheduler:
  - critical: input/camera
  - medium: nearby visuals
  - low: distant effects

### 5.2 Render Lag
- Dynamic resolution scaling (optional if launcher supports)
- Level-of-detail factor for particles/entity shadows
- Occlusion heuristics refinement

### 5.3 Chunk Stutter
- Async chunk mesh prepare
- Main thread upload pacing
- Render-distance ramp-up (sudden jump avoid)

### 5.4 Memory Pressure Lag
- Object pooling for frequent temporary buffers
- Controlled cache sizes
- GC-friendly allocations (avoid per-frame garbage)

### 5.5 Network Burst Lag (PvP)
- Packet decode burst smoothing client-side
- Visual event coalescing (same tick me repeated tiny effects combine)

## 6) Device profile system (bahut important)
Runtime me phone detect nahi, but performance detect karo:
- Boot benchmark: 20s warmup + micro profile
- Device tier assign:
  - Tier A (high-end)
  - Tier B (mid)
  - Tier C (low)
- Each tier has default caps:
  - Render distance max
  - Particle budget
  - Chunk upload budget/frame
  - Entity visual budget

## 7) JNI/C++ integration (agar zaroori ho)

## JNI flow
1. Java calls native `init()`
2. Native feature support detect (SIMD/ABI)
3. If fail -> Java fallback automatically

## Required ABIs for Android ecosystem
- arm64-v8a mandatory
- armeabi-v7a optional (legacy)
- x86_64 optional (emulators)

## Safety rules
- Native crash guard via strict boundary checks
- No blocking JNI call in render thread
- Return immutable/simple structs for low overhead

## 8) "Vulkan jaisa" expectation ka practical answer
- Vanilla Java client pipeline ko fully Vulkan me shift karna Fabric mod level pe easy nahi.
- Jo practical hai:
  - draw-call pressure kam karna
  - frame pacing improve karna
  - adaptive quality
  - CPU/GPU sync stalls reduce karna
- Result: **stable smoothness improvement** mil sakti hai; exact `3x` fixed guarantee nahi.

## 9) Fabric 1.21.4 project skeleton (example)

```text
src/main/java/com/lagkill/
  LagKillMod.java
  config/
    LagKillConfig.java
  monitor/
    FrameTimeMonitor.java
    TickBudgetMonitor.java
  render/
    DynamicQualityController.java
    ChunkUploadPacer.java
  entity/
    EntityUpdateLimiter.java
  pvp/
    CombatProfileController.java
  nativebridge/
    NativeBridge.java
src/main/resources/
  fabric.mod.json
  lagkill.mixins.json
src/main/cpp/
  native_bridge.cpp
  simd_math.cpp
```

## 10) Recommended rollout plan

### Phase 1 (MVP)
- In-game overlay: FPS + frame-time graph + MSPT
- Dynamic particle limiter
- Adaptive render distance
- Basic chunk upload pacing

### Phase 2
- Entity visual budget system
- Combat profile toggle
- Config GUI presets (Performance / Balanced / Quality)

### Phase 3
- Optional JNI module
- Auto benchmark on first launch
- Crash-safe fallback and telemetry logs

## 11) Testing matrix
- Resolution: 720p / 900p / 1080p
- Render distance: 8 / 12 / 16 / 20
- Scenario sets:
  1. Flat world sprint
  2. Dense base with hoppers/redstone
  3. PvP duel with particles
  4. Elytra fast travel chunk streaming

Metrics:
- Avg FPS
- 1% low FPS
- 0.1% low FPS
- frame-time std-dev
- stutter events per minute (>50ms)

## 12) Anti-overpromise policy (must)
Mod description me kabhi ye fixed claims mat dena:
- "Sab phones me same performance"
- "Always 3x FPS"
- "Future saare lags permanently fixed"

Iske badle:
- "Adaptive optimization based on real-time load"
- "Lower stutter and better frame pacing on supported setups"
- "Safe fallbacks for unsupported/native-fail environments"

## 13) Next step (implementation)
Agar tum chaho to next iteration me:
1. `fabric.mod.json`
2. `LagKillConfig.java`
3. `FrameTimeMonitor.java`
4. `DynamicQualityController.java`
5. `NativeBridge.java` (JNI-ready with fallback)

ka starter code ready kar sakta hoon.
