package club.lyric.infinity.api.util.client.keyboard;

import org.lwjgl.glfw.GLFW;

public enum KeyPressAction {
    PRESS,
    REPEAT,
    RELEASE;

    public static KeyPressAction get(int action) {
        if (action == GLFW.GLFW_PRESS) {
            return PRESS;
        } else if (action == GLFW.GLFW_RELEASE) {
            return RELEASE;
        } else return REPEAT;
    }
}