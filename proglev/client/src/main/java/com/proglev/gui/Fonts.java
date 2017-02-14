package com.proglev.gui;

import javafx.scene.text.Font;

/**
 * Created by kyko on 2/14/2017.
 */
public class Fonts {

    public static final Font FONT_AWSOME_14 = fontAwsome(14);
    public static final Font FONT_AWSOME_20 = fontAwsome(20);

    private static Font fontAwsome(int size) {
        return Font.loadFont(Fonts.class.getResource("/public/assets/font-awsome/fonts/FontAwesome.otf").toExternalForm(), size);
    }
}
