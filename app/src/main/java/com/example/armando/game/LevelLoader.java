package com.example.armando.game;

import android.util.Log;

import com.badlogic.androidgames.framework.FileIO;
import com.example.armando.game.actors.Actor;
import com.example.armando.game.actors.Bandit;
import com.example.armando.game.actors.Crate;
import com.example.armando.game.actors.FryingPan;
import com.example.armando.game.actors.Hangman;
import com.example.armando.game.actors.Player;
import com.example.armando.game.actors.Rope;
import com.example.armando.game.components.PhysicsComponent;
import com.example.armando.game.components.SpriteComponent;
import com.example.armando.game.levels.GameLevel;
import com.example.armando.game.managers.PixmapManager;
import com.google.fpl.liquidfun.BodyType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    private final FileIO fileIO;

    public LevelLoader(FileIO fileIO) {
        this.fileIO = fileIO;
    }

    /**
     * Carica gli attori da un file LDTK.
     */
    public List<Actor> loadActors(GameLevel levelInstance, String fileName) {
        List<Actor> actors = new ArrayList<>();
        Player player = null;
        try {
            String json = readFileAsString(fileName);
            JSONObject root = new JSONObject(json);
            JSONArray layerInstances = root.getJSONArray("layerInstances");
            for (int i = 0; i < layerInstances.length(); i++) {
                JSONObject layer = layerInstances.getJSONObject(i);
                if (!layer.has("entityInstances")) continue;

                JSONArray entities = layer.getJSONArray("entityInstances");
                for (int j = 0; j < entities.length(); j++) {
                    JSONObject entity = entities.getJSONObject(j);

                    String identifier = entity.getString("__identifier");
                    JSONArray px = entity.getJSONArray("px");
                    float x = (float) px.getInt(0);
                    float y = (float) px.getInt(1);

                    float width = (float) entity.getInt("width");
                    float height = (float) entity.getInt("height");

                    float angle = 0f;

                    boolean facingRight = true;

                    if (entity.has("fieldInstances")) {
                        JSONArray fields = entity.getJSONArray("fieldInstances");
                        for (int k = 0; k < fields.length(); k++) {
                            JSONObject field = fields.getJSONObject(k);
                            if (field.getString("__identifier").equals("angle")) {
                                angle = (float) field.getDouble("__value");
                            } else if (field.getString("__identifier").equals("facingRight")) {
                                facingRight = field.getBoolean("__value");
                            }
                        }
                    }

                    switch (identifier) {
                        case "FryingPan":
                            boolean hanged = false;
                            float deflectionAngle = 0;
                            int ropeLength = 0;
                            JSONArray fields = entity.getJSONArray("fieldInstances");
                            for (int k = 0; k < fields.length(); k++) {
                                JSONObject field = fields.getJSONObject(k);
                                if (field.getString("__identifier").equals("deflectionAngle")) {
                                    deflectionAngle = (float) field.getDouble("__value");
                                } else if (field.getString("__identifier").equals("hanged")) {
                                    hanged = field.getBoolean("__value");
                                } else if (field.getString("__identifier").equals("ropeLength")) {
                                    ropeLength = field.getInt("__value");
                                }
                            }
                            if (hanged) {
                                Rope rope = new Rope(levelInstance, x, y, ropeLength, Rope.RopeType.SOFT, FryingPan::new);
                                actors.add(rope);
                                ((FryingPan)rope.endActor).deflectionAngle = deflectionAngle;
                            } else {
                                actors.add(new FryingPan(levelInstance, x, y, deflectionAngle));
                            }
                            break;
                        case "Bandit":
                            actors.add(new Bandit(levelInstance, x, y, facingRight));
                            break;
                        case "Ground":
                            actors.add(new Actor(levelInstance, x, y, List.of(
                                    new SpriteComponent(
                                            PixmapManager.getPixmap("environment/ground.png"),
                                            320, 4, 1
                                    )
                            )));
                            break;
                        case "Gallows":
                            actors.add(new Actor(levelInstance, x, y, List.of(
                                    new SpriteComponent(
                                            PixmapManager.getPixmap("environment/gallows.png"),
                                            32, 40, 1
                                    )
                            )));
                            break;
                        case "Arc_Platform":
                            actors.add(new Actor(levelInstance, x, y, List.of(
                                    new SpriteComponent(
                                            PixmapManager.getPixmap("environment/arc_platform.png")
                                    ),
                                    new PhysicsComponent(
                                            levelInstance,
                                            BodyType.staticBody,
                                            Coordinates.pixelsToMetersLengthsX(16),
                                            Coordinates.pixelsToMetersLengthsY(32)
                                    )
                            )));
                            break;
                        case "Platform":
                            Actor platform = new Actor(levelInstance, x, y, List.of(
                                    new SpriteComponent(
                                            PixmapManager.getPixmap("environment/platform_0.png")
                                    )
                            ));
                            platform.angle = (float) Math.toRadians(angle);
                            platform.addComponent(
                                    new PhysicsComponent(
                                            levelInstance,
                                            BodyType.staticBody,
                                            Coordinates.pixelsToMetersLengthsX(38),
                                            Coordinates.pixelsToMetersLengthsY(6f)
                                    )
                            );
                            actors.add(platform);
                            break;
                        case "Hangman":
                            actors.add(new Rope(levelInstance, x, y, 4, Rope.RopeType.SOFT, Hangman::new));
                            break;
                        case "Collision":
                            actors.add(new Actor(
                                    levelInstance,
                                    x,
                                    y,
                                    List.of(
                                            new PhysicsComponent(
                                                    levelInstance,
                                                    BodyType.staticBody,
                                                    Coordinates.pixelsToMetersLengthsX(width),
                                                    Coordinates.pixelsToMetersLengthsY(height)
                                            )
                                    )
                            ));
                            break;
                        case "Crate":
                            actors.add(new Crate(levelInstance, x, y));
                            break;

                        case "Rock":
                            actors.add(new Actor(levelInstance, x, y, List.of(
                                    new SpriteComponent(
                                            PixmapManager.getPixmap("environment/rock1.png"),
                                            18, 9, 1
                                    ),
                                    new PhysicsComponent(
                                            levelInstance,
                                            BodyType.dynamicBody,
                                            Coordinates.pixelsToMetersLengthsX(18),
                                            Coordinates.pixelsToMetersLengthsY(7.5f)
                                    )
                            )));
                            break;

                        case "PlayerStart":
                            player = new Player(levelInstance, x, y, levelInstance.game.getInput());
                            actors.add(player);
                            break;

                        default:
                            Log.w("LevelLoader", "Entità non gestita: " + identifier);
                            break;
                    }
                }
            }

        } catch (Exception e) {
            Log.e("LevelLoader", "Errore caricamento livello", e);
        }

        return actors;
    }

    /**
     * Legge un file di testo dagli assets usando FileIO.
     */
    private String readFileAsString(String fileName) throws Exception {
        try (InputStream is = fileIO.readAsset(fileName)) {
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        }
    }
}
