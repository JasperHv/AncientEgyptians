package nl.vu.cs.softwaredesign.ui.scenes;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import nl.vu.cs.softwaredesign.ui.menus.MainMenu;
import org.jetbrains.annotations.NotNull;

public class GameSceneFactory extends SceneFactory {

    @NotNull
    @Override
    public FXGLMenu newMainMenu() {
        return new MainMenu();
    }

    @NotNull
    @Override
    public FXGLMenu newGameMenu() {
        return new SimpleGameMenu();
    }

}
