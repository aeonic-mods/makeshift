package design.aeonic.makeshift.api.client.ui;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.nifty.api.client.ui.Texture;
import design.aeonic.nifty.api.client.ui.template.StaticUiElementTemplate;

public class MkUiSets {

    public static class Crude {

        public static final Texture TEXTURE_MAP = new Texture(Makeshift.location("textures/gui/template/crude.png"));

        public static final StaticUiElementTemplate BACKGROUND = new StaticUiElementTemplate(TEXTURE_MAP, 176, 166, 80, 90);

    }

    public static class Kinetic {

        public static final Texture TEXTURE_MAP = new Texture(Makeshift.location("textures/gui/template/kinetic.png"));

        public static final StaticUiElementTemplate BACKGROUND = new StaticUiElementTemplate(TEXTURE_MAP, 176, 166, 80, 90);

    }

    public static class Digital {

        public static final Texture TEXTURE_MAP = new Texture(Makeshift.location("textures/gui/template/silicon.png"));

        public static final StaticUiElementTemplate BACKGROUND = new StaticUiElementTemplate(TEXTURE_MAP, 176, 166, 80, 90);

    }

    public static class Vanguard {

        public static final Texture TEXTURE_MAP = new Texture(Makeshift.location("textures/gui/template/kinetic.png"));

        public static final StaticUiElementTemplate BACKGROUND = new StaticUiElementTemplate(TEXTURE_MAP, 176, 166, 80, 90);

    }

    public static class Creative {

        public static final Texture TEXTURE_MAP = new Texture(Makeshift.location("textures/gui/template/creative.png"));

        public static final StaticUiElementTemplate BACKGROUND = new StaticUiElementTemplate(TEXTURE_MAP, 176, 166, 80, 90);

    }

}
