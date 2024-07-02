package club.lyric.infinity.api.util.client.sounds;

import club.lyric.infinity.Infinity;
import org.apache.commons.io.IOUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.InputStream;

public class SoundsUtils {

    public static void playSound(String filename, int volume) {
        InputStream inputStream = null;
        try {
            inputStream = SoundsUtils.class.getResourceAsStream("/assets/infinity/sounds/" + filename);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            Clip audioClip = AudioSystem.getClip();

            audioClip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);

            gainControl.setValue((((float) volume) * 40f / 100f) - 35f);
            audioClip.start();
        } catch (Exception e) {
            Infinity.LOGGER.atError();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}