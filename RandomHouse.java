import java.awt.*;
import java.awt.Graphics;
import java.awt.Color;
import static java.lang.Integer.parseInt;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

public class RandomHouse extends JPanel {
    /*variables declaration having drawing definitions
    Height and width of the rectangle and triangular roof
    */
    int width=0;
    int height=0;
    
    /*RGB values for the base color*/
    int base1=0;
    int base2=0;
    int base3=0;
    
    /*RGB values for the roof color*/
    int roof1=0;
    int roof2=0;
    int roof3=0;
    
    /*RGB values for the flowers color*/
    int flower1=0;
    int flower2=0;
    int flower3=0;
    
    String basesh="";
    String roof="";
    String flowersh="";
    
    /*Get a random variate between 50 and 150*/
    public void lengthWidth(){

        int length = 0;
        length = 50 + (int)(Math.random() * 150);
        width=length;
        height=length*(5/2);
    }
    

    /* global string returning a random color*/
    public Color randomColor(String type){

        int R = (int)(Math.random()*256);
        int G = (int)(Math.random()*256);
        int B= (int)(Math.random()*256);
        
        Color color = new Color(R, G, B);
        if(type.equals("flowers")){
            flower1=R;
            flower2=G;
            flower3=G;
        }
        
        if(type.equals("base")){
            base1=R;
            base2=G;
            base3=B;
        }
        if(type.equals("triangle")){
            roof1=R;
            roof2=G;
            roof3=B;
        }
        
        return color;
        
    }
    
    
    /*Paint the canvas on the JPanel*/
    public void paintComponent(Graphics paint){
        super.paintComponent(paint);
        this.setBackground(Color.WHITE);
       
        lengthWidth();
        Color base=randomColor("base");
        paint.setColor(base);
        paint.fillRect(200,200,height,width);
        
        /*A triangle has half the height of the base*/
        paint.setColor(randomColor("triangle"));
        int x[]={200,200+height,200+height/2};
        int y[]={200,200,150-(width/2)};
        Polygon p=new Polygon(x,y,3);
        
    
        paint.fillPolygon(p);
            
        
        //write text
        paint.setColor(Color.red);
        
        paint.drawString("House length:"+height,40,10);
        paint.drawString("House height:"+width,40,25);
        paint.drawString("House base color : rgb("+base1+","+base2+","+base3+")",40,40);
        paint.drawString("House roof color : rgb("+roof1+","+roof2+","+roof3+")",40,55);
        paint.drawString("Flower color :  rgb("+flower1+","+flower2+","+flower3+")",40,70);
        
        //flowers
        Color petal=randomColor("flowers");
        //loop to draw more flowers
        for(int space=0;space<=500;){
            Graphics2D g2 = (Graphics2D) paint;

            // Draw the stem.
            g2.setStroke(
                new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(0, 128, 0));  // green
            g2.drawLine(toScreenX(100+space), toScreenY(400), toScreenX(100+space), toScreenY(350));

            // Draw the center.
            g2.setColor(new Color(255, 165, 0));  // orange
            g2.fillOval(toScreenX(85+space), toScreenY(340), toScreenX(30), toScreenY(30));

            // Draw eight petals at N, NE, E, SE, S, SW, W, NW positions on the center.
            int petalWidth = toScreenX(20);
            int petalHeight = toScreenY(20);
            g2.setColor(petal);  // pink
            
            
            g2.fillOval(toScreenX(90+space), toScreenY(320), petalWidth, petalHeight);
            g2.fillOval(toScreenX(90+space), toScreenY(370), petalWidth, petalHeight);
            g2.fillOval(toScreenX(64+space), toScreenY(350), petalWidth, petalHeight);
            g2.fillOval(toScreenX(115+space), toScreenY(350), petalWidth, petalHeight);
            g2.fillOval(toScreenX(108+space), toScreenY(368), petalWidth, petalHeight);
            g2.fillOval(toScreenX(108+space), toScreenY(332), petalWidth, petalHeight);
            g2.fillOval(toScreenX(72+space), toScreenY(368), petalWidth, petalHeight);
            g2.fillOval(toScreenX(72+space), toScreenY(332), petalWidth, petalHeight);

            space+=100;
            
        }
    
    }
    /** Converts an x-coordinate from a 200-width screen to the actual width. */
    private int toScreenX(int x) {
        return x;
    }

    /** Converts an y-coordinate from a 200-width screen to the actual width. */
    private int toScreenY(int y) {
        return y;
    }
    public static void main(String[] args){
        JFrame frame = new JFrame("Peter Mwikya - RandomHouse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        
        RandomHouse randomhouse = new RandomHouse();

        frame.add(randomhouse);

        frame.setVisible(true);


    }
    
}