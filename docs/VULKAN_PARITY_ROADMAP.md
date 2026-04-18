# Vulkan-style Performance Roadmap (for LagKill)

## Reality first
- **VulkanMod** ek renderer rewrite project hai (OpenGL renderer ko Vulkan pipeline se replace karta hai).
- Current LagKill abhi renderer rewrite nahi, balki adaptive tuning/perf controller layer hai.
- Isliye immediate goal: **VulkanMod ke sath compatible high-smooth presets** + **stutter reduction**.

## What we learned from VulkanMod repo
Reference repo: https://github.com/xCollateral/VulkanMod/tree/dev

Unke README ke hisaab se major ideas:
1. Renderer modernization and Vulkan 1.2 path.
2. Reduced CPU overhead.
3. Multiple chunk culling optimizations.
4. Indirect draw pipeline style optimizations.
5. Graphic settings and queue controls.

## LagKill implementation plan to get closer

### Phase A (already started)
- Runtime overload monitor
- Recording-safe profile switching
- Vulkan mod presence detection
- Renderer preference config (`AUTO`, `DEFAULT`, `VULKAN_OPTIMIZED`)

### Phase B (next, medium effort)
- Chunk update budget scheduler
- Entity/particle budget governor
- P95 frame-time driven dynamic queue target
- Config GUI with per-profile overrides

### Phase C (advanced)
- JNI-backed hot loops (culling math, frustum tests)
- Optional parallel visibility passes
- Telemetry snapshots for profile auto-learning

### Phase D (hard / research)
- Renderer hooks + draw-call batching integration layer
- Vulkan-specific pathing only when VulkanMod installed and compatible version detected

## Mobile launcher constraints (important)
- Pojav/Fold/etc. launcher behavior alag hota hai; driver/thermal limits vary.
- `400 FPS fixed` claim safe nahi across all devices.
- Better target: stable low frame-time variance + fewer stutters while recording.

## Recommended benchmark target
- 1080p, render distance 12-16, recording on
- Success criteria:
  - p95 frame-time < 22ms
  - stutter events (>50ms) < 4/min
  - no crash in 30 min continuous run
