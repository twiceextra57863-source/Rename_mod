#include <jni.h>

extern "C" JNIEXPORT jint JNICALL
Java_com_rename_1mod_lagkill_nativebridge_NativeBridge_nativeSuggestFpsCap(
    JNIEnv* env,
    jclass,
    jdouble p95Ms,
    jint desiredCap,
    jboolean multiplayer
) {
    int cap = desiredCap;

    if (multiplayer == JNI_TRUE) {
        if (p95Ms > 32.0) {
            cap = cap > 144 ? 144 : cap;
        } else if (p95Ms > 24.0) {
            cap = cap > 165 ? 165 : cap;
        }
    } else {
        if (p95Ms > 38.0) {
            cap = cap > 120 ? 120 : cap;
        } else if (p95Ms > 28.0) {
            cap = cap > 165 ? 165 : cap;
        }
    }

    if (cap < 90) {
        cap = 90;
    }

    return cap;
}
