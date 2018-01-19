package convolution;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


public class ConvProc {
    
    WritableRaster out, read;
    public static float[][] blur = new float[][]{{1, 1, 1},{1, 1, 1},{1, 1, 1}};
    public static float[][] gblur = new float[][]{{1, 2, 1},{2, 4, 2},{1, 2, 1}};
    public static float[][] edge = new float[][]{{-1,-1,-1},{-1,8,-1},{-1,-1,-1}};
    
    public void process(BufferedImage img, float[][] kernel){
        int w = img.getWidth();
        int h = img.getHeight();
        float sum = sum(kernel);
        int msize = kernel[0].length;
        out = img.getRaster();
        // use only out raster for some effects...
        read = img.copyData(null);
        for (int x = 0; x < w; x++){
            for (int y = 0; y < h; y++){ 
                out.setDataElements(x, y, convolve(read, x, y, w, h, kernel, msize, sum));
            }
        }        
    }

    public void process(BufferedImage img, float[][] kernel, boolean greyscale, int thresh){
        int w = img.getWidth();
        int h = img.getHeight();
        float sum = sum(kernel);
        int msize = kernel[0].length;
        out = img.getRaster();
        byte[] vals;
        // use only out raster for some effects...
        read = img.copyData(null);
        for (int x = 0; x < w; x++){
            for (int y = 0; y < h; y++){ 
                if(!greyscale){
                    vals = convolve(read, x, y, w, h, kernel, msize, sum, thresh);
                }else{
                    vals = convolveGrey(read, x, y, w, h, kernel, msize, sum, thresh);
                }
                out.setDataElements(x, y, vals);
            }
        }        
    }
    
    private byte[] convolve(Raster img, int x, int y, int w, int h, float[][] mat, int matsize, float sum){
        float r = 0, g = 0, b = 0;
        int offs = matsize/2;
        byte[] vals;
        for (int i = 0; i < matsize; i++){
            for (int j = 0; j < matsize; j++){
                vals = (byte[])img.getDataElements(constrain(x+(i-offs),0,w-1), constrain(y+(j-offs),0,h-1), null);
                r += (vals[0]&0xff) * mat[i][j];
                g += (vals[1]&0xff) * mat[i][j];
                b += (vals[2]&0xff) * mat[i][j];
            }
        } 
        r = constrain((int)(r/sum),0,255);
        g = constrain((int)(g/sum),0,255);
        b = constrain((int)(b/sum),0,255);
        return new byte[]{(byte)r,(byte)g,(byte)b};
    }

    private byte[] convolve(Raster img, int x, int y, int w, int h, float[][] mat, int matsize, float sum, int thresh){
        float r = 0, g = 0, b = 0;
        int offs = matsize/2;
        byte[] vals;
        for (int i = 0; i < matsize; i++){
            for (int j = 0; j < matsize; j++){
                vals = (byte[])img.getDataElements(constrain(x+(i-offs),0,w-1), constrain(y+(j-offs),0,h-1), null);
                r += (vals[0]&0xff) * mat[i][j];
                g += (vals[1]&0xff) * mat[i][j];
                b += (vals[2]&0xff) * mat[i][j];
            }
        }
        if(thresh > 0){
            r = thresh((int)(r/sum),thresh);
            g = thresh((int)(g/sum),thresh);
            b = thresh((int)(b/sum),thresh);            
        }else{
            r = constrain((int)(r/sum),0,255);
            g = constrain((int)(g/sum),0,255);
            b = constrain((int)(b/sum),0,255);            
        }
        return new byte[]{(byte)r,(byte)g,(byte)b};
    }
    
    private byte[] convolveGrey(Raster img, int x, int y, int w, int h, float[][] mat, int matsize, float sum, int thresh){
        float c, val = 0;
        int offs = matsize/2;
        for (int i = 0; i < matsize; i++){
            for (int j = 0; j < matsize; j++){
                c = getL((byte[])img.getDataElements(constrain(x+(i-offs),0,w-1), constrain(y+(j-offs),0,h-1), null));
                val += c * mat[i][j];
            }
        }
        if(thresh > 0){
            val = thresh((int)(val/sum),thresh);
        }else{
            val = constrain((int)(val/sum),0,255);   
        }
        return new byte[]{(byte)val,(byte)val,(byte)val};
    }    

    private static float getL(byte[] in){  
        return ((in[0]&0xff) + (in[1]&0xff) + (in[2]&0xff))/3f;
    } 

    private static int thresh(int in, int thresh){
        return in >= thresh ? 255 : 0;
    }
    
    private static int constrain(int in, int min, int max){
        return in < min ? 0 : in > max ? max : in;
    }
   
    private float sum(float[][] m){
        int l1 = m[0].length;
        int l2 = m[1].length;
        float rtn = 0;
        for(int i = 0; i < l1; i++){
          for(int j = 0; j < l2; j++){
            rtn += m[i][j];
          }
        }
        if(rtn < 1 && rtn > -1){ rtn = 1; }
        return rtn;
    }
    
}
