
package convolution;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


public class ConvProc {
    
    Raster out;
    static float[][] blur = new float[][]{{1,1,1},{1,1,1},{1,1,1}};

    
    void process(BufferedImage img, float[][] kernel){
        WritableRaster raster = img.getRaster();
//        out = raster.createCompatibleWritableRaster();
      //  raster.g
        int w = img.getWidth();
        int h = img.getHeight();
        float sum = sum(kernel);
        int msize = kernel[0].length;
        
        for (int x = 0; x < w; x++){
            for (int y = 0; y < h; y++){
                float[] vals = convolve(raster, x, y, w, h, kernel, msize, sum);
                raster.setPixel(x, y, vals);       
            }
        }
        
      //  return new BufferedImage(img.getColorModel(), out, img.isAlphaPremultiplied(), null);
    }
    
    float[] convolve(Raster img, int x, int y, int w, int h, float[][] mat, int matsize, float sum){
        float r = 0, g = 0, b = 0;
        int offs = matsize/2;
        float scale = 1/sum;
        byte[] vals = new byte[3];
        for (int i = 0; i < matsize; i++){
            for (int j = 0; j < matsize; j++){
               vals = (byte[])img.getDataElements(constrain(x+(i-offs),0,w-1), constrain(y+(j-offs),0,h-1), null);
               r += vals[0] * mat[i][j];
               g += vals[1] * mat[i][j];
               b += vals[2] * mat[i][j];
            }
        }
        return new float[]{r/sum,g/sum,b/sum};
       // return new float[]{r,g,b};
    }
    
    int constrain(int in, int min, int max){
//        if(in < min)
//            return min;
//        if(in > max)
//            return max;
//        return in;
        return Math.max(0,Math.min(in, max));
    }
    
    static float sum(float[][] m){
        int l1 = m[0].length;
        int l2 = m[1].length;
        float rtn = 0;
        for(int i = 0; i < l1; i++){
          for(int j = 0; j < l2; j++){
            rtn += m[i][j];
          }
        }
        return rtn;
    }
    
    static float getR(int in){
        return ((in & 0xff0000) >> 16)/255f;
    }
    static float getG(int in){
        return ((in & 0xff00) >> 8)/255f;
    }
    static float getB(int in){
        return (in & 0xff)/255f;
    } 
}
