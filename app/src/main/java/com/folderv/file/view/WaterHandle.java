package com.folderv.file.view;

import android.graphics.Bitmap;

import java.nio.IntBuffer;
/*
 * Name:     Water
 * Date:     December 2004
 * Author:   Neil Wallis
 * Purpose:  Simulate ripples on water.
 */


public class WaterHandle 
{
    private Bitmap oldmap;

	public WaterHandle(Bitmap oldmap, Bitmap dest) {
		this.oldmap = oldmap;
		init();
	}

	String str;
    int width,height,hwidth,hheight;
    //MemoryImageSource source;
    //Image image, offImage;
    //Graphics offGraphics;
    int i,a,b;
    int MouseX,MouseY;
    int fps,delay,size;

    short ripplemap[];
    int texture[];
    int ripple[];
    int oldind,newind,mapind;
    int riprad;
    IntBuffer dst;
    //Image im;

    Thread animatorThread;
    boolean frozen = false;

    public void init() {
      //Retrieve the base image
//      str = getParameter("image");
//      if (str != null) {
//        try {
//          MediaTracker mt = new MediaTracker(this);
//          im = getImage(getDocumentBase(),str);
//          mt.addImage(im,0);
//          try {
//            mt.waitForID(0);
//            } catch (InterruptedException e) {
//              return;
//            }
//        } catch(Exception e) {}
//      }

      width = oldmap.getWidth();
      height = oldmap.getHeight();
      hwidth = width>>1;
      hheight = height>>1;
      riprad=3;

      size = width * (height+2) * 2;
      ripplemap = new short[size];
      ripple = new int[width*height];
      texture = new int[width*height];
      oldind = width;
      newind = width * (height+3);

      oldmap.getPixels(texture, 0, width, 0, 0, width, height);
//      PixelGrabber pg = new PixelGrabber(im,0,0,width,height,texture,0,width);
//      try {
//        pg.grabPixels();
//        } catch (InterruptedException e) {}

      oldmap.getPixels(ripple, 0, width, 0, 0, width, height);
//      source = new MemoryImageSource(width, height, ripple, 0, width);
//      source.setAnimated(true);
//      source.setFullBufferUpdates(true);

     // image = createImage(source);
      //offImage = createImage(width, height);
     // offGraphics = offImage.getGraphics();
      dst = IntBuffer.allocate(width*height);
    }

//    public void mouseMoved(MouseEvent e) {
//	disturb(e.getX(),e.getY());
//    }

    public void run1() {
      //while (Thread.currentThread() == animatorThread) {
        newframe();
        //dst.put(ripple);dst.rewind();
        //dest.setPixels(ripple, 0, width, 0, 0, width, height);
        //dest.copyPixelsFromBuffer(dst);
        
        
        //source.newPixels();
        //offGraphics.drawImage(image,0,0,width,height,null);
        //repaint();

      //}
    }

//    public void paint(Graphics g) {
//      update(g);
//    }

//    public void update(Graphics g) {
//      g.drawImage(offImage,0,0,this);
//    }

    public void disturb(int dx, int dy) {
      for (int j=dy-riprad;j<dy+riprad;j++) {
        for (int k=dx-riprad;k<dx+riprad;k++) {
          if (j>=0 && j<height && k>=0 && k<width) {
	    ripplemap[oldind+(j*width)+k] += 512;            
          } 
        }
      }
    }

    public void newframe() {
      //Toggle maps each frame
      i=oldind;
      oldind=newind;
      newind=i;

      i=0;
      mapind=oldind;
      for (int y=0;y<height;y++) {
        for (int x=0;x<width;x++) {
	  short data = (short)((ripplemap[mapind-width]+ripplemap[mapind+width]+ripplemap[mapind-1]+ripplemap[mapind+1])>>1);
          data -= ripplemap[newind+i];
          data -= data >> 5;
          ripplemap[newind+i]=data;

	  //where data=0 then still, where data>0 then wave
	  data = (short)(1024-data);

          //offsets
  	  a=((x-hwidth)*data/1024)+hwidth;
          b=((y-hheight)*data/1024)+hheight;

 	  //bounds check
          if (a>=width) a=width-1;
          if (a<0) a=0;
          if (b>=height) b=height-1;
          if (b<0) b=0;

          ripple[i]=texture[a+(b*width)];
          mapind++;
	  i++;
        }
      }
    }

	public int[] getOut() {
		// TODO Auto-generated method stub
		return ripple;
	}

}

