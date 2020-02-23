package game.framework.visual;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.objects.GameObject;
import game.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Prashant Chaubey
 * Created On: 23-02-2020 18:05
 * Purpose: Load texture for various game objects
 **/
public class TextureLoader {
    private static final TextureLoader INSTANCE = new TextureLoader();
    private Map<GameObject.GameObjectType, Texture> textureInfo;

    private TextureLoader() {
        this.textureInfo = new HashMap<>();
        try {
            init();

        } catch (IOException e) {
            System.out.println("Error in loading textures");
            e.printStackTrace();
        }
    }

    public static TextureLoader getInstance() {
        return INSTANCE;
    }

    private void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream in = TextureLoader.class.getResourceAsStream(Constants.TEXTURE_CONFIG_FILE_NAME)) {
            //Loading data from json
            TextureConfig[] configs = mapper.readValue(in, TextureConfig[].class);

            for (TextureConfig config : configs) {
                //Skipping in case of failure
                try {
                    Texture texture = Texture.fromTextureConfig(config);
                    textureInfo.put(texture.getType(), texture);

                } catch (Exception e) {
                    System.out.println(String.format("Error while loading texture configuration for type: %s", config.getType()));
                    e.printStackTrace();
                }
            }
        }
    }

    public Texture getTexture(GameObject.GameObjectType type) {
        return textureInfo.get(type);
    }
}
