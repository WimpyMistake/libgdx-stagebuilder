package net.peakgames.libgdx.stagebuilder.core.builder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.peakgames.libgdx.stagebuilder.core.assets.AssetsInterface;
import net.peakgames.libgdx.stagebuilder.core.assets.ResolutionHelper;
import net.peakgames.libgdx.stagebuilder.core.model.BaseModel;
import net.peakgames.libgdx.stagebuilder.core.model.ButtonModel;
import net.peakgames.libgdx.stagebuilder.core.services.LocalizationService;
import net.peakgames.libgdx.stagebuilder.core.util.GdxUtils;
import net.peakgames.libgdx.stagebuilder.core.util.NinePatchUtils;

public class ButtonBuilder extends ActorBuilder {

    private static final int MIN_VISIBLE_AREA_FOR_PATCH_REGION = 2;
    
    protected BaseDrawable up;
    protected BaseDrawable down;
    protected BaseDrawable disabled;
    protected BaseDrawable checked;

    public ButtonBuilder(AssetsInterface assets, ResolutionHelper resolutionHelper, LocalizationService localizationService) {
        super(assets, resolutionHelper, localizationService);
    }

    @Override
    public Actor build(BaseModel model) {
        ButtonModel buttonModel = (ButtonModel) model;
        setTextures(buttonModel);
        Button button = new Button(up, down);
        if (buttonModel.getFrameDisabled() != null) {
            button.getStyle().disabled = disabled;
        }
        if ( buttonModel.getFrameChecked() != null){
            button.getStyle().checked = checked;
        }
        normalizeModelSize(buttonModel, up.getMinWidth(), up.getMinHeight());
        setBasicProperties(model, button);
        return button;
    }

    protected void setTextures(ButtonModel buttonModel) {
        if (buttonModel.getTextureSrcUp() != null) {
            createTexturesFromSrc(buttonModel);
        } else {
            createTexturesFromAtlas(buttonModel);
        }
        
        if (buttonModel.getTintColorUp() != null) {
            up = getTintedDrawable(up, buttonModel.getTintColorUp());
        }

        if (buttonModel.getTintColorDown() != null) {
            down = getTintedDrawable(down, buttonModel.getTintColorDown());
        }

        if (buttonModel.getTintColorDisabled() != null) {
            disabled = getTintedDrawable(disabled, buttonModel.getTintColorDisabled());
        }

        if (buttonModel.getTintColorChecked() != null) {
            checked = getTintedDrawable(checked, buttonModel.getTintColorChecked());
        }
    }
    
    private BaseDrawable getTintedDrawable(BaseDrawable drawable, String color) {
        if (drawable instanceof SpriteDrawable) {
            return ((SpriteDrawable) drawable).tint(Color.valueOf(color));
        } else if (drawable instanceof NinePatchDrawable) {
            return ((NinePatchDrawable) drawable).tint(Color.valueOf(color));
        } 
        
        return null;
    }

    private void createTexturesFromAtlas(ButtonModel model) {
        TextureAtlas textureAtlas = assets.getTextureAtlas(model.getAtlasName());

        if (model.isNinePatch()) {
            TextureRegion upRegion = flipRegion(textureAtlas.findRegion(model.getFrameUp()), model);
            
            normalizeNinePatchValues(model);
            normalizeHorizontalPatches(model, upRegion);
            normalizeVerticalPatches(model, upRegion);
            
            up = NinePatchUtils.createNinePatchDrawableFromAtlas(resolutionHelper, model.getFrameUp(),
                    textureAtlas, model.getNpLeft(), model.getNpRight(),
                    model.getNpTop(), model.getNpBottom(), model.isFlipX(), model.isFlipY());
            
            down = NinePatchUtils.createNinePatchDrawableFromAtlas(resolutionHelper, model.getFrameDown(),
                    textureAtlas, model.getNpLeft(), model.getNpRight(),
                    model.getNpTop(), model.getNpBottom(), model.isFlipX(), model.isFlipY());

            if (model.getFrameDisabled() != null) {
                disabled = NinePatchUtils.createNinePatchDrawableFromAtlas(resolutionHelper, 
                        model.getFrameDisabled(), textureAtlas, model.getNpLeft(), 
                        model.getNpRight(), model.getNpTop(), model.getNpBottom(), model.isFlipX(), model.isFlipY());
            }
            
            if (model.getFrameChecked() != null) {
                checked = NinePatchUtils.createNinePatchDrawableFromAtlas(resolutionHelper,
                        model.getFrameChecked(), textureAtlas, model.getNpLeft(), 
                        model.getNpRight(), model.getNpTop(), model.getNpBottom(), model.isFlipX(), model.isFlipY());
            }
        } else {
            this.up = new SpriteDrawable(new Sprite(GdxUtils.flipRegion(
                    textureAtlas.findRegion(model.getFrameUp()), model.isFlipX(), model.isFlipY())));
            this.down = new SpriteDrawable(new Sprite(GdxUtils.flipRegion(
                    textureAtlas.findRegion(model.getFrameDown()), model.isFlipX(), model.isFlipY())));
            
            if (model.getFrameDisabled() != null) {
                this.disabled = new SpriteDrawable(new Sprite(GdxUtils.flipRegion(
                        textureAtlas.findRegion(model.getFrameDisabled()), model.isFlipX(), model.isFlipY())));
            }
            if (model.getFrameChecked() != null) {
                this.checked = new SpriteDrawable(new Sprite(GdxUtils.flipRegion(
                        textureAtlas.findRegion(model.getFrameChecked()), model.isFlipX(), model.isFlipY())));
            }
        }
    }

    private void createTexturesFromSrc(ButtonModel model) {
        if (model.isNinePatch()) {
            normalizeNinePatchValues(model);

            TextureRegion upRegion = flipRegion(new TextureRegion(new Texture(model.getTextureSrcUp())), model);
            up = convertTextureRegionToNinePatchDrawable(
                    upRegion, model.getNpLeft(), model.getNpRight(), model.getNpTop(), model.getNpBottom());

            TextureRegion downRegion = flipRegion(new TextureRegion(new Texture(model.getTextureSrcDown())), model);
            down = convertTextureRegionToNinePatchDrawable(
                    downRegion, model.getNpLeft(), model.getNpRight(), model.getNpTop(), model.getNpBottom());

            if (model.getFrameDisabled() != null) {
                TextureRegion region = flipRegion(new TextureRegion(new Texture(model.getTextureSrcDisabled())), model);
                disabled = convertTextureRegionToNinePatchDrawable(
                        region, model.getNpLeft(), model.getNpRight(), model.getNpTop(), model.getNpBottom());
            }

            if (model.getFrameChecked() != null) {
                TextureRegion region = flipRegion(new TextureRegion(new Texture(model.getTextureSrcChecked())), model);
                checked = convertTextureRegionToNinePatchDrawable(
                        region, model.getNpLeft(), model.getNpRight(), model.getNpTop(), model.getNpBottom());
            }
        } else {
            this.down = new TextureRegionDrawable(
                    flipRegion(new Sprite(new Texture(model.getTextureSrcDown())), model));
            this.up = new TextureRegionDrawable(
                    flipRegion(new Sprite(new Texture(model.getTextureSrcUp())), model));
            if (model.getTextureSrcDisabled() != null) {
                this.disabled = new TextureRegionDrawable(
                        flipRegion(new Sprite(new Texture(model.getTextureSrcDisabled())), model));
            }
            if (model.getTextureSrcChecked() != null) {
                this.checked = new TextureRegionDrawable(
                        flipRegion(new Sprite(new Texture(model.getTextureSrcChecked())), model));
            }
        }
    }
    
    private TextureRegion flipRegion(TextureRegion region, ButtonModel buttonModel) {
        if (buttonModel.isFlipX() || buttonModel.isFlipY()) {
            return GdxUtils.flipRegion(region, buttonModel.isFlipX(), buttonModel.isFlipY());
        }
        
        return region;
    }

    private void normalizeNinePatchValues(ButtonModel buttonModel) {
        buttonModel.setNpLeft((int) (buttonModel.getNpLeft() * resolutionHelper.getPositionMultiplier()));
        buttonModel.setNpRight((int)(buttonModel.getNpRight() * resolutionHelper.getPositionMultiplier()));
        buttonModel.setNpTop((int)(buttonModel.getNpTop() * resolutionHelper.getPositionMultiplier()));
        buttonModel.setNpBottom((int)(buttonModel.getNpBottom() * resolutionHelper.getPositionMultiplier()));
    }

    private void normalizeHorizontalPatches(ButtonModel buttonModel, TextureRegion npDrawable) {
        int totalPatchWidth = buttonModel.getNpLeft() + buttonModel.getNpRight();
        if(totalPatchWidth >= npDrawable.getRegionWidth()) {
            int usableAreaWidth = calculateMinUsableAreaSize(npDrawable.getRegionWidth());
            buttonModel.setNpLeft(calculateNewPatchSize(usableAreaWidth, buttonModel.getNpLeft(), totalPatchWidth));
            buttonModel.setNpRight(calculateNewPatchSize(usableAreaWidth, buttonModel.getNpRight(), totalPatchWidth));
        }
    }

    private void normalizeVerticalPatches(ButtonModel buttonModel, TextureRegion npDrawable) {
        int totalPatchHeight = buttonModel.getNpTop() + buttonModel.getNpBottom();
        if(totalPatchHeight >= npDrawable.getRegionHeight()) {
            int usableAreaHeight = calculateMinUsableAreaSize(npDrawable.getRegionHeight());
            buttonModel.setNpTop(calculateNewPatchSize(usableAreaHeight, buttonModel.getNpTop(), totalPatchHeight ));
            buttonModel.setNpBottom(calculateNewPatchSize(usableAreaHeight, buttonModel.getNpBottom(),
                    totalPatchHeight ));
        }
    }

    private static int calculateMinUsableAreaSize(int size) {
        return size - MIN_VISIBLE_AREA_FOR_PATCH_REGION;
    }

    private static int calculateNewPatchSize(int usableAreaSize, int patchSize, int totalPatchSize) {
        return (int)(usableAreaSize * (patchSize / (float)totalPatchSize));
    }
}
