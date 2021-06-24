package com.example.AugmentedRealityProject88;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArFragment arFragment;
    private ModelRenderable Astro,Robo;
    private String Model_URL = "https://modelviewer.dev/shared-assets/models/Astronaut.glb",
            Model_URL2 = "https://modelviewer.dev/shared-assets/models/RobotExpressive.glb",
            Model_URL3 = "https://modelviewer.dev/shared-assets/models/Horse.glb";

    ImageView astro,robo;
    View array[];
    int selected = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        astro = findViewById(R.id.astro);
        robo = findViewById(R.id.robo);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        SetArrayView();
        SetClickListener();
        setUpModel();
        setUpPlane();
    }

    private void SetClickListener() {
        for (int i = 0; i <array.length;i++)
        {
            array[i].setOnClickListener(this);
        }
    }

    private void SetArrayView() {
        array = new View[]{astro,robo};
    }

    private void setUpModel() {
        ModelRenderable.builder()
                .setSource(this,
                        RenderableSource.builder().setSource(
                                this,
                                Uri.parse(Model_URL),
                                RenderableSource.SourceType.GLB)
                                .setScale(0.20f)
                                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                .build())
                .setRegistryId(Model_URL)
                .build()
                .thenAccept(renderable -> Astro = renderable)
                .exceptionally(throwable -> {
                    Log.i("Model","cant load");
                    Toast.makeText(MainActivity.this,"Model can't be Loaded", Toast.LENGTH_SHORT).show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this,
                        RenderableSource.builder().setSource(
                                this,
                                Uri.parse(Model_URL2),
                                RenderableSource.SourceType.GLB)
                                .setScale(0.20f)
                                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                .build())
                .setRegistryId(Model_URL2)
                .build()
                .thenAccept(renderable -> Robo = renderable)
                .exceptionally(throwable -> {
                    Log.i("Model","cant load");
                    Toast.makeText(MainActivity.this,"Model can't be Loaded", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void setUpPlane(){
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            createModel(anchorNode);
        }));
    }

    private void createModel(AnchorNode anchorNode){
        if (selected == 1){
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(Astro);
            node.select();}
        else if (selected == 2){
            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setParent(anchorNode);
            node.setRenderable(Robo);
            node.select();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.astro)
            selected = 1;
        else if (view.getId() == R.id.robo)
            selected = 2;
    }
}
