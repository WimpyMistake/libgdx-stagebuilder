package net.peakgames.libgdx.stagebuilder.core.builder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class ShadowLabel extends Label {

    private Label shadowLabel;

    public ShadowLabel(CharSequence text, LabelStyle labelStyle, String shadowColorName) {
        this(text, labelStyle, Color.valueOf(shadowColorName));
    }

    public ShadowLabel(CharSequence text, LabelStyle labelStyle, Color shadowColor) {
        super(text,labelStyle);
        LabelStyle shadowStyle = new LabelStyle(labelStyle.font, shadowColor);
        shadowLabel = new Label(text, shadowStyle);
        float shadowX = getX()+6;
        float shadowY = getY()-6;
        shadowLabel.setPosition(shadowX, shadowY);
    }

    @Override
    public void setZIndex(int index) {
        super.setZIndex(index);
        shadowLabel.setZIndex(index+100);
    }

    @Override
    public void setFontScale(float fontScale) {
        super.setFontScale(fontScale);
        shadowLabel.setFontScale(fontScale);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shadowLabel.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void setText(CharSequence newText) {
        super.setText(newText);
        shadowLabel.setText(newText);
    }

    @Override
    public void setAlignment(int labelAlign, int lineAlign) {
        super.setAlignment(labelAlign, lineAlign);
        shadowLabel.setAlignment(labelAlign, lineAlign);
    }

    public void setShadowColor(Color color){
        shadowLabel.setColor(color);
    }

    @Override
    public void setPosition(float x, float y) {
        shadowLabel.setPosition(x+1, y-1);
        super.setPosition(x, y);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        shadowLabel.setBounds(x+1, y-1, width, height);
        super.setBounds(x, y, width, height);
    }

    public Label getShadowLabel() {
        return shadowLabel;
    }

    public void setShadowLabel(Label shadowLabel) {
        this.shadowLabel = shadowLabel;
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        int index = parent.getChildren().indexOf(this, true);
        parent.addActorAt(index, shadowLabel);
    }

    @Override
    public void validate() {
        super.validate();
        shadowLabel.validate();
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        shadowLabel.setX(x+1);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        shadowLabel.setY(y-1);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        shadowLabel.setVisible(visible);
    }

    @Override
    public void setWrap(boolean wrap) {
        super.setWrap(wrap);
        shadowLabel.setWrap(wrap);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        if(shadowLabel != null){
            shadowLabel.setSize(width, height);
        }
    }
}