package com.uwsoft.editor.renderer.data;

import java.util.ArrayList;

public class CompositeVO {

    public ArrayList<SimpleImageVO> sImages = new ArrayList<>(1);
    public ArrayList<Image9patchVO> sImage9patchs = new ArrayList<>(1);
    public ArrayList<TextBoxVO> sTextBox = new ArrayList<>(1);
    public ArrayList<ButtonVO> sButtons = new ArrayList<>(1);
    public ArrayList<LabelVO> sLabels = new ArrayList<>(1);
    public ArrayList<CompositeItemVO> sComposites = new ArrayList<>(1);
    public ArrayList<CheckBoxVO> sCheckBoxes = new ArrayList<>(1);
    public ArrayList<SelectBoxVO> sSelectBoxes = new ArrayList<>(1);
    public ArrayList<ParticleEffectVO> sParticleEffects = new ArrayList<>(1);
    public ArrayList<LightVO> slights = new ArrayList<>(1);
    public ArrayList<SpineVO> sSpineAnimations = new ArrayList<>(1);
    public ArrayList<SpriteAnimationVO> sSpriteAnimations = new ArrayList<>(1);

    public ArrayList<LayerItemVO> layers = new ArrayList<LayerItemVO>();

    public CompositeVO() {

    }

    public CompositeVO(CompositeVO vo) {

        if (vo == null) return;

        update(vo);
    }

    public void update(CompositeVO vo) {
        clear();
        for (int i = 0; i < vo.sImages.size(); i++) {
            sImages.add(new SimpleImageVO(vo.sImages.get(i)));
        }
        for (int i = 0; i < vo.sImage9patchs.size(); i++) {
            sImage9patchs.add(new Image9patchVO(vo.sImage9patchs.get(i)));
        }
        for (int i = 0; i < vo.sTextBox.size(); i++) {
            sTextBox.add(new TextBoxVO(vo.sTextBox.get(i)));
        }
        for (int i = 0; i < vo.sButtons.size(); i++) {
            sButtons.add(new ButtonVO(vo.sButtons.get(i)));
        }
        for (int i = 0; i < vo.sLabels.size(); i++) {
            sLabels.add(new LabelVO(vo.sLabels.get(i)));
        }
        for (int i = 0; i < vo.sComposites.size(); i++) {
            sComposites.add(new CompositeItemVO(vo.sComposites.get(i)));
        }

        for (int i = 0; i < vo.sCheckBoxes.size(); i++) {
            sCheckBoxes.add(new CheckBoxVO(vo.sCheckBoxes.get(i)));
        }
        for (int i = 0; i < vo.sSelectBoxes.size(); i++) {
            sSelectBoxes.add(new SelectBoxVO(vo.sSelectBoxes.get(i)));
        }

        for (int i = 0; i < vo.sParticleEffects.size(); i++) {
            sParticleEffects.add(new ParticleEffectVO(vo.sParticleEffects.get(i)));
        }

        for (int i = 0; i < vo.slights.size(); i++) {
            slights.add(new LightVO(vo.slights.get(i)));
        }

        for (int i = 0; i < vo.sSpineAnimations.size(); i++) {
            sSpineAnimations.add(new SpineVO(vo.sSpineAnimations.get(i)));
        }

        for (int i = 0; i < vo.sSpriteAnimations.size(); i++) {
            sSpriteAnimations.add(new SpriteAnimationVO(vo.sSpriteAnimations.get(i)));
        }


        layers.clear();
        for (int i = 0; i < vo.layers.size(); i++) {
            layers.add(new LayerItemVO(vo.layers.get(i)));
        }

    }

    public void addItem(MainItemVO vo) {
        String className = vo.getClass().getSimpleName().toString();

        if (className.equals("SimpleImageVO")) {
            sImages.add((SimpleImageVO) vo);
        }
        if (className.equals("TextBoxVO")) {
            sTextBox.add((TextBoxVO) vo);
        }
        if (className.equals("ButtonVO")) {
            sButtons.add((ButtonVO) vo);
        }
        if (className.equals("LabelVO")) {
            sLabels.add((LabelVO) vo);
        }
        if (className.equals("CompositeItemVO")) {
            sComposites.add((CompositeItemVO) vo);
        }
        if (className.equals("CheckBoxVO")) {
            sCheckBoxes.add((CheckBoxVO) vo);
        }
        if (className.equals("SelectBoxVO")) {
            sSelectBoxes.add((SelectBoxVO) vo);
        }
        if (className.equals("ParticleEffectVO")) {
            sParticleEffects.add((ParticleEffectVO) vo);
        }
        if (className.equals("LightVO")) {
            slights.add((LightVO) vo);
        }
        if (className.equals("SpineVO")) {
            sSpineAnimations.add((SpineVO) vo);
        }
        if (className.equals("SpriteAnimationVO")) {
            sSpriteAnimations.add((SpriteAnimationVO) vo);
        }
    }

    public void removeItem(MainItemVO vo) {
        String className = vo.getClass().getSimpleName().toString();
        if (className.equals("SimpleImageVO")) {
            sImages.remove((SimpleImageVO) vo);
        }
        if (className.equals("TextBoxVO")) {
            sTextBox.remove((TextBoxVO) vo);
        }
        if (className.equals("ButtonVO")) {
            sButtons.remove((ButtonVO) vo);
        }
        if (className.equals("LabelVO")) {
            sLabels.remove((LabelVO) vo);
        }
        if (className.equals("CompositeItemVO")) {
            sComposites.remove((CompositeItemVO) vo);
        }
        if (className.equals("CheckBoxVO")) {
            sCheckBoxes.remove((CheckBoxVO) vo);
        }
        if (className.equals("SelectBoxVO")) {
            sSelectBoxes.remove((SelectBoxVO) vo);
        }
        if (className.equals("ParticleEffectVO")) {
            sParticleEffects.remove((ParticleEffectVO) vo);
        }
        if (className.equals("LightVO")) {
            slights.remove((LightVO) vo);
        }
        if (className.equals("SpineVO")) {
            sSpineAnimations.remove((SpineVO) vo);
        }
        if (className.equals("SpriteAnimationVO")) {
            sSpriteAnimations.remove((SpriteAnimationVO) vo);
        }
    }

    public void clear() {
        sImages.clear();
        sTextBox.clear();
        sButtons.clear();
        sLabels.clear();
        sComposites.clear();
        sCheckBoxes.clear();
        sSelectBoxes.clear();
        sParticleEffects.clear();
        slights.clear();
        sSpineAnimations.clear();
        sSpriteAnimations.clear();
    }

}
