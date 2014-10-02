package com.uwsoft.editor.renderer.data;

import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.resources.FontSizePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

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
    public ArrayList<LightVO> sLights = new ArrayList<>(1);
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

        for (int i = 0; i < vo.sLights.size(); i++) {
            sLights.add(new LightVO(vo.sLights.get(i)));
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
        String className = vo.getClass().getSimpleName();

        if (className.equals("SimpleImageVO")) {
            sImages.add((SimpleImageVO) vo);
        }
        if (className.equals("Image9patchVO")) {
            sImage9patchs.add((Image9patchVO) vo);
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
            sLights.add((LightVO) vo);
        }
        if (className.equals("SpineVO")) {
            sSpineAnimations.add((SpineVO) vo);
        }
        if (className.equals("SpriteAnimationVO")) {
            sSpriteAnimations.add((SpriteAnimationVO) vo);
        }
    }

    public void removeItem(MainItemVO vo) {
        String className = vo.getClass().getSimpleName();
        if (className.equals("SimpleImageVO")) {
            sImages.remove((SimpleImageVO) vo);
        }
        if (className.equals("Image9patchVO")) {
            sImage9patchs.remove((Image9patchVO) vo);
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
            sLights.remove((LightVO) vo);
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
        sLights.clear();
        sSpineAnimations.clear();
        sSpriteAnimations.clear();
    }

    public boolean isEmpty() {
        return sComposites.size() == 0 &&
                sImage9patchs.size() == 0 &&
                sImages.size() == 0 &&
                sSpriteAnimations.size() == 0 &&
                sButtons.size() == 0 &&
                sCheckBoxes.size() == 0 &&
                sLabels.size() == 0 &&
                sLights.size() == 0 &&
                sParticleEffects.size() == 0 &&
                sCheckBoxes.size() == 0 &&
                sSpriteAnimations.size() == 0 &&
                sSpineAnimations.size() == 0 &&
                sSelectBoxes.size() == 0 &&
                sTextBox.size() == 0;
    }

    public String[] getRecursiveParticleEffectsList() {
        HashSet<String> list = new HashSet<>();
        for (ParticleEffectVO sParticleEffect : sParticleEffects) {
            list.add(sParticleEffect.particleName);
        }
        for (CompositeItemVO sComposite : sComposites) {
            String[] additionalList = sComposite.composite.getRecursiveParticleEffectsList();
            Collections.addAll(list, additionalList);
        }
        String[] finalList = new String[list.size()];
        list.toArray(finalList);

        return finalList;
    }

    public String[] getRecursiveSpineAnimationList() {
        HashSet<String> list = new HashSet<>();
        for (SpineVO sSpineAnimation : sSpineAnimations) {
            list.add(sSpineAnimation.animationName);
        }
        for (CompositeItemVO sComposite : sComposites) {
            String[] additionalList = sComposite.composite.getRecursiveSpineAnimationList();
            Collections.addAll(list, additionalList);
        }
        String[] finalList = new String[list.size()];
        list.toArray(finalList);

        return finalList;
    }

    public String[] getRecursiveSpriteAnimationList() {
        HashSet<String> list = new HashSet<>();
        for (SpriteAnimationVO sSpriteAnimation : sSpriteAnimations) {
            list.add(sSpriteAnimation.animationName);
        }
        for (CompositeItemVO sComposite : sComposites) {
            String[] additionalList = sComposite.composite.getRecursiveSpriteAnimationList();
            Collections.addAll(list, additionalList);
        }
        String[] finalList = new String[list.size()];
        list.toArray(finalList);

        return finalList;
    }

    public FontSizePair[] getRecursiveFontList() {
        HashSet<FontSizePair> list = new HashSet<>();
        for (LabelVO sLabel : sLabels) {
            list.add(new FontSizePair(sLabel.style.isEmpty() ? "arial" : sLabel.style, sLabel.size == 0 ? 12 : sLabel.size));
        }
        for (CompositeItemVO sComposite : sComposites) {
            FontSizePair[] additionalList = sComposite.composite.getRecursiveFontList();
            Collections.addAll(list, additionalList);
        }
        FontSizePair[] finalList = new FontSizePair[list.size()];
        list.toArray(finalList);

        return finalList;
    }

    public ArrayList<MainItemVO> getAllItems() {
        ArrayList<MainItemVO> itemsList = new ArrayList<>();
        itemsList = getAllItemsRecursive(itemsList, this);

        return itemsList;
    }

    private ArrayList<MainItemVO> getAllItemsRecursive(ArrayList<MainItemVO> itemsList, CompositeVO compositeVo) {
        for(MainItemVO vo: compositeVo.sButtons) {
            itemsList.add(vo);
        }
        for(MainItemVO vo: compositeVo.sCheckBoxes) {
            itemsList.add(vo);
        }
        for(MainItemVO vo: compositeVo.sImage9patchs) {
            itemsList.add(vo);
        }
        for(MainItemVO vo: compositeVo.sImages) {
            itemsList.add(vo);
        }
        for(MainItemVO vo: compositeVo.sLabels) {
            itemsList.add(vo);
        }
        for(MainItemVO vo: compositeVo.sLights) {
            itemsList.add(vo);
        }
        for(MainItemVO vo: compositeVo.sParticleEffects) {
            itemsList.add(vo);
        }
        for(MainItemVO vo: compositeVo.sSelectBoxes) {
            itemsList.add(vo);
        }
        for(MainItemVO vo: compositeVo.sSpineAnimations) {
            itemsList.add(vo);
        }
        for(MainItemVO vo: compositeVo.sSpriteAnimations) {
            itemsList.add(vo);
        }
        for(MainItemVO vo: compositeVo.sTextBox) {
            itemsList.add(vo);
        }
        for(CompositeItemVO vo: compositeVo.sComposites) {
            itemsList = getAllItemsRecursive(itemsList,vo.composite);
            itemsList.add(vo);
        }

        return itemsList;
    }
}
