package com.guxuede.mc.loader;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;


import com.guxuede.math.Vector3;
import com.momchil_atanasov.data.front.parser.IOBJParser;
import com.momchil_atanasov.data.front.parser.OBJDataReference;
import com.momchil_atanasov.data.front.parser.OBJFace;
import com.momchil_atanasov.data.front.parser.OBJMesh;
import com.momchil_atanasov.data.front.parser.OBJModel;
import com.momchil_atanasov.data.front.parser.OBJObject;
import com.momchil_atanasov.data.front.parser.OBJParser;
import org.bukkit.Material;

public class ModelObjectLoader implements ObjectLoader {

    private Pigment pen;
    private OBJModel model;
    private int faceVertex;
    private PaintPointBox box;
    private int totalFace;
    private int hitTotalFace;

    private int availableTotalFace;

    public ModelObjectLoader(int faceVertex,int material) {
        this.faceVertex = faceVertex;
        pen = new Pigment(Material.getMaterial(material), (byte) 1);
    }



    @Override
    public PaintPointBox load(InputStream in) throws NotSupportResource, IOException {
        box = new PaintPointBox();
        // Create an OBJParser and parse the resource
        final IOBJParser parser = new OBJParser();
        model = parser.parse(in);

        // Use the model representation to get some basic info
        System.out.println(MessageFormat.format(
                "OBJ model has {0} vertices, {1} normals, {2} texture coordinates, and {3} objects.",
                model.getVertices().size(),
                model.getNormals().size(),
                model.getTexCoords().size(),
                model.getObjects().size()));

        if(faceVertex == 0){
            drawObject();
            System.out.println("drawObject");
        }else if(faceVertex == 1){
            System.out.println("onlyDrawVertex");
            onlyDrawVertex();
        }
        System.out.println("Face:"+hitTotalFace +"/"+ availableTotalFace + "/" +totalFace);
        return box;
    }

    public void onlyDrawVertex(){
        for(Vector3 p : model.getVertices()){
            onBlock(p);
        }
    }

    public void drawObject(){
        OBJObject obj = model.getObjects().get(0);
        List<OBJMesh> meshes = obj.getMeshes();
        for(int i=0;i<meshes.size();i++){
            OBJMesh mesh = meshes.get(i);
            System.out.println("Analysis mesh:"+i+"/"+meshes.size());
            drawMeshes(mesh);
        }
    }

    public void drawMeshes(OBJMesh mesh){
        List<OBJFace> faces = mesh.getFaces();
        for(int i = 0;i< faces.size();i++){
            OBJFace face = faces.get(i);
            System.out.println("Analysis face:"+i+"/"+faces.size());
            List<OBJDataReference> ref = face.getReferences();
            if(ref.size() == 3){
                Vector3 v1 = model.getVertices().get(ref.get(0).vertexIndex);
                Vector3 v2 = model.getVertices().get(ref.get(1).vertexIndex);
                Vector3 v3 = model.getVertices().get(ref.get(2).vertexIndex);
                drawTriangleFace(v1,v2,v3);
            }else{
                throw new RuntimeException("not support other Vertex with : "+ ref.size());
            }
        }
    }

    public void drawQuadrilateralFace(Vector3 v1,Vector3 v2,Vector3 v3,Vector3 v4){
        drawTriangleFace(v1,v2,v3);
        drawTriangleFace(v2,v3,v4);
    }
    public void drawTriangleFace(Vector3 v1,Vector3 v2,Vector3 v3){
        float minx = min(v1.x, v2.x, v3.x);
        float maxx = max(v1.x, v2.x,v3.x);
        float miny = min(v1.y, v2.y,v3.y);
        float maxy = max(v1.y, v2.y,v3.y);
        float minz = min(v1.z, v2.z,v3.z);
        float maxz = max(v1.z, v2.z,v3.z);
        boolean hasYes = false;
        boolean hasAvaliable = false;
        for(float x = minx ; x <= maxx ; x++){
            for(float y = miny ; y <= maxy ; y++){
                for(float z = minz ; z <= maxz ; z++){
                    hasAvaliable = true;
                    Vector3 p = new Vector3(x,y,z);
                    if(pointinTriangle(v1, v2, v3, p)){
                        hasYes = true;
                        onBlock(p);
                    }
                }
            }
        }
        totalFace++;
        if(hasYes){
            hitTotalFace++;
        }
        if(hasAvaliable){
            availableTotalFace++;
        }
    }
    public void onBlock(Vector3 p){
        PaintPoint point = new PaintPoint(pen, p);
        box.addPoint(point);
    }

    public boolean pointinTriangle(Vector3 A, Vector3 B, Vector3 C, Vector3 P)
    {
        float angles = 0;

        Vector3 v1 = Vector3.min(P, A); v1.normalize();
        Vector3 v2 = Vector3.min(P, B); v2.normalize();
        Vector3 v3 = Vector3.min(P, C); v3.normalize();

        angles += Math.acos(Vector3.dot(v1, v2));
        angles += Math.acos(Vector3.dot(v2, v3));
        angles += Math.acos(Vector3.dot(v3, v1));

        return (Math.abs(angles - 2*Math.PI) <= 0.005);
    }

    public static float min(float a,float b,float c){
        return Math.min(Math.min(a, b),c);
    }
    public static float max(float a,float b,float c){
        return Math.max(Math.max(a, b),c);
    }
}
