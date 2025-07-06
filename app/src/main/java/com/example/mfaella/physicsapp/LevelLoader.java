package com.example.mfaella.physicsapp;

import android.util.Log;

import com.badlogic.androidgames.framework.FileIO;
import com.badlogic.androidgames.framework.Game;
import com.example.mfaella.physicsapp.actors.Actor;
import com.example.mfaella.physicsapp.actors.Bandit;
import com.example.mfaella.physicsapp.actors.Crate;
import com.example.mfaella.physicsapp.actors.DeflectionTry;
import com.example.mfaella.physicsapp.actors.Hangman;
import com.example.mfaella.physicsapp.actors.Player;
import com.example.mfaella.physicsapp.actors.Rope;
import com.example.mfaella.physicsapp.actors.ui.Clock;
import com.example.mfaella.physicsapp.components.PhysicsComponent;
import com.example.mfaella.physicsapp.components.SpriteComponent;
import com.example.mfaella.physicsapp.levels.GameLevel;
import com.example.mfaella.physicsapp.managers.PixmapManager;
import com.example.mfaella.physicsapp.actors.ui.Button;
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
//
//            JSONArray levels = root.getJSONArray("levels");
//
//            JSONObject firstLevel = levels.getJSONObject(0);

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

                    switch (identifier) {
                        case "Bandit":
                            actors.add(new Bandit(x, y));
                            break;
                        case "Ground":
                            actors.add(new Actor(x, y, List.of(
                                    new SpriteComponent(
                                            PixmapManager.getPixmap("environment/ground.png"),
                                            320, 4, 1
                                    )
                            )));
                            break;
                        case "Gallows":
                            actors.add(new Actor(x, y, List.of(
                                    new SpriteComponent(
                                            PixmapManager.getPixmap("environment/gallows.png"),
                                            32, 40, 1
                                    )
                            )));
                            break;
                        case "Arc_Platform":
                            actors.add(new Actor(x, y, List.of(
                                    new SpriteComponent(
                                            PixmapManager.getPixmap("environment/arc_platform.png")
                                    ),
                                    new PhysicsComponent(
                                            BodyType.staticBody,
                                            Coordinates.pixelsToMetersLengthsX(16),
                                            Coordinates.pixelsToMetersLengthsY(32)
                                    )
                            )));
                            break;
                        case "Platform":
                            actors.add(new Actor(x, y, List.of(
                                    new SpriteComponent(
                                            PixmapManager.getPixmap("environment/platform_0.png")
                                    ),
                                    new PhysicsComponent(
                                            BodyType.staticBody,
                                            Coordinates.pixelsToMetersLengthsX(38),
                                            Coordinates.pixelsToMetersLengthsY(6f)
                                    )
                            )));
                            break;
                        case "Hangman":
                            actors.add(new Rope(x, y, 4, Rope.RopeType.SOFT, Hangman::new));
                            break;
                        case "Collision":
                            actors.add(new Actor(
                                    x,
                                    y,
                                    List.of(
                                            new PhysicsComponent(
                                                    BodyType.staticBody,
                                                    Coordinates.pixelsToMetersLengthsX(width),
                                                    Coordinates.pixelsToMetersLengthsY(height)
                                            )
                                    )
                            ));
                            break;
                        case "Crate":
                            actors.add(new Crate(x, y));
                            break;

                        case "DeflectionTry":
                            actors.add(new DeflectionTry(x, y));
                            break;

                        case "Rock":
                            actors.add(new Actor(x, y, List.of(
                                    new SpriteComponent(
                                            PixmapManager.getPixmap("environment/rock1.png"),
                                            18, 9, 1
                                    ),
                                    new PhysicsComponent(
                                            BodyType.dynamicBody,
                                            Coordinates.pixelsToMetersLengthsX(18),
                                            Coordinates.pixelsToMetersLengthsY(7.5f)
                                    )
                            )));
                            break;

                        case "PlayerStart":
                            player = new Player(x, y, levelInstance.game.getInput());
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

        // UI
        if (player != null) {
            actors.add(new Button(levelInstance, 32, 158, "ui/bang_btn.png",player.gun::shoot));
            actors.add(new Rope(305, 0, 6, Rope.RopeType.HARD, Clock::new));
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
