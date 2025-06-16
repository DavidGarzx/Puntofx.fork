/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unicenta.pos.scale;

import gnu.io.*;
import java.io.*;
import java.util.TooManyListenersException;


/**
 *
 * @author alvarogv
 */
public class GenericScaleXD implements Scale, SerialPortEventListener {
    
    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;      
    private String m_sPortScale;
    private OutputStream m_out;
    private InputStream m_in;
    private static final int SCALE_READY = 0;
    private static final int SCALE_READING = 1;
    private static final int SCALE_READINGDECIMALS = 2;
    private static int SCALE_NOMORE = 1;    
    private double m_dWeightBuffer;
    private double m_dWeightDecimals;
    private int m_iStatusScale;
    
    
    public GenericScaleXD(String sPortPrinter){
        
           this.m_sPortScale = sPortPrinter;
	    this.m_out = null;
	    this.m_in = null;
	    this.m_iStatusScale = 0;
	    this.m_dWeightBuffer = 0.0;
	    this.m_dWeightDecimals = 1.0;    
    
    }
    
    public Double readWeight(){
    
            synchronized (this)
	    {
	      if (this.m_iStatusScale != 0) {
	        try {
	          super.wait(1000L);
	        } catch (InterruptedException e) {
	        }
	        if (this.m_iStatusScale != 0)
	        {
	          this.m_iStatusScale = 0;
	        }

	      }

	      this.m_dWeightBuffer = 0.0;
	      this.m_dWeightDecimals = 1.0;
	      write(new byte[] {80});

	      flush();
	      try
	      {
	        super.wait(1000L);
	      }
	      catch (InterruptedException e) {
	      }
	      if (this.m_iStatusScale == 0)
	      {
	        double dWeight = this.m_dWeightBuffer / this.m_dWeightDecimals;
	        this.m_dWeightBuffer = 0.0;
	        this.m_dWeightDecimals = 1.0;

	        return new Double(dWeight);
	      }
	      this.m_iStatusScale = 0;
	      this.m_dWeightBuffer = 0.0;
	      this.m_dWeightDecimals = 1.0;
	      return new Double(0.0D);
	    }
	  }
    
    	  private void flush()
	  {
	    try {
	      this.m_out.flush();
	    } catch (IOException e) {
	    }
	  }
    
    	  private void write(byte[] data) {
	    try {
	      if (this.m_out == null) {
	        this.m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(this.m_sPortScale);
	        this.m_CommPortPrinter = ((SerialPort)this.m_PortIdPrinter.open("PORTID", 2000));

	        this.m_out = this.m_CommPortPrinter.getOutputStream();
	        this.m_in = this.m_CommPortPrinter.getInputStream();

	        this.m_CommPortPrinter.addEventListener(this);
	        this.m_CommPortPrinter.notifyOnDataAvailable(true);

	        this.m_CommPortPrinter.setSerialPortParams(9600, 8, 1, 0);
	      }

	      this.m_out.write(data);
	    } catch (NoSuchPortException e) {
	      e.printStackTrace();
	    } catch (PortInUseException e) {
	      e.printStackTrace();
	    } catch (UnsupportedCommOperationException e) {
	      e.printStackTrace();
	    } catch (TooManyListenersException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
    
    
    	  public void serialEvent(SerialPortEvent e)
	  {
	    switch (e.getEventType())
	    {
	    case 2:
	    case 3:
	    case 4:
	    case 5:
	    case 6:
	    case 7:
	    case 8:
	    case 9:
	    case 10:
	      break;
	    case 1:
	      try
	      {
	        while (this.m_in.available() > 0)
	        {
	          int b = this.m_in.read();
	          if (b == 13)
	          {
	            synchronized (this) {
	              this.m_iStatusScale = 0;
	              super.notifyAll(); }
	          }
	          else if (((b > 47) && (b < 58)) || (b == 46))
	            synchronized (this)
	            {
	              if (this.m_iStatusScale == 0) {
	                this.m_dWeightBuffer = 0.0D;
	                this.m_dWeightDecimals = 1.0D;
	                this.m_iStatusScale = 1;
	              }
	              if (b == 46) {
	                this.m_iStatusScale = 2;
	              } else {
	                this.m_dWeightBuffer = (this.m_dWeightBuffer * 10.0D + b - 48.0D);

	                if (this.m_iStatusScale == 2)
	                  this.m_dWeightDecimals *= 10.0D;
	              }
	            }
	        }
	      }
	      catch (IOException eIO)
	      {
	      }
	    }
	  }
    
    
}
