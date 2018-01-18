
package convolution;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


public class ConvProc {
    
    WritableRaster out;
    static float[][] blur = new float[][]{{1,1,1},{1,1,1},{1,1,1}};

    
    void process(BufferedImage img, float[][] kernel){
        int w = img.getWidth();
        int h = img.getHeight();
        float sum = sum(kernel);
        int msize = kernel[0].length;
        out = img.getRaster();
        for (int x = 0; x < w; x++){
            for (int y = 0; y < h; y++){
                out.setDataElements(x, y, convolve(out, x, y, w, h, kernel, msize, sum));
            }
        }        
    }
    
  byte[] convolve(Raster img, int x, int y, int w, int h, float[][] mat, int matsize, float sum){
        float r = 0, g = 0, b = 0;
        int offs = matsize/2;
        byte[] vals = new byte[]{0,0,0};
        for (int i = 0; i < matsize; i++){
            for (int j = 0; j < matsize; j++){
                vals = (byte[])img.getDataElements(constrain(x+(i-offs),0,w-1), constrain(y+(j-offs),0,h-1), null);
                r += (vals[0]&0xff) * mat[i][j];
                g += (vals[1]&0xff) * mat[i][j];
                b += (vals[2]&0xff) * mat[i][j];
            }
        } 
        return new byte[]{(byte)(r/sum),(byte)(g/sum),(byte)(b/sum)};
    }
    
    static int constrain(int in, int min, int max){
        if(in < min)
            return min;
        if(in > max)
            return max;
        return in;
//        return Math.max(0,Math.min(in, max));
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
    
    static int rgb(int r, int g, int b){
        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        return rgb;
    }
    static int rgb(float r, float g, float b){
        int rgb = (int)r;
        rgb = (rgb << 8) + (int)g;
        rgb = (rgb << 8) + (int)b;
        return rgb;
    }
//    static int getR(int in){
//        return ((in & 0xff0000) >> 16);
//    }
//    static int getG(int in){
//        return ((in & 0xff00) >> 8);
//    }
//    static int getB(int in){
//        return (in & 0xff);
//    }     
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
