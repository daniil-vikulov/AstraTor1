package Physics;

import Tools.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ball extends Body {
    private double radius;
    boolean isStatic = false;


    public Ball(Vector c, double m, double radius, Material material) {
        super(c, m);
        ball = true;
        this.radius = radius;
        this.material = material;
    }
    public Ball(Vector c, double m){
        super(c,m);
        radius = 0;
    }

    @Override
    public void collision(Body body) {
        if (body.ball) {
            Ball b = (Ball) body;
            if (c.distanceTo(b.c) < radius + b.radius) {
                double kS = (material.getK() + b.material.getK()) / 2;
                double push = (radius + b.radius - c.distanceTo(b.c)) / 2;
                Vector axis = new Vector(b.c.x - c.x, b.c.y - c.y);
                Vector an = new Vector(axis.x / Math.sqrt(axis.x * axis.x + axis.y * axis.y), axis.y / Math.sqrt(axis.x * axis.x + axis.y * axis.y));
                double a = Math.sqrt(an.x * an.x + an.y * an.y);
                double cos = an.x / a;
                double sin = an.y / a;
                c.x -= push * cos;
                c.y -= push * sin;
                b.c.x += push * cos;
                b.c.y += push * sin;
                Vector vR1 = new Vector(cV.x * cos + cV.y * sin, -cV.x * sin + cV.y * cos);
                Vector vR2 = new Vector(b.cV.x * cos + b.cV.y * sin, -b.cV.x * sin + b.cV.y * cos);
                /*Vector uR1 = new Vector((vR1.x*(m - b.m) + 2*b.m*vR2.x)/(m + b.m), vR1.y);
                Vector uR2 = new Vector((vR2.x*(b.m - m) + 2*m*vR1.x)/(m + b.m), vR2.y);*/
                Vector uR1 = new Vector(vR1.plus(vR2.minus(vR1).x(((kS + 1) * b.m) / (m + b.m))));
                Vector uR2 = new Vector(vR2.minus(vR2.minus(vR1).x(((kS + 1) * b.m) / (m + b.m))));
                Vector u1 = new Vector(uR1.x * cos - uR1.y * sin, uR1.x * sin + uR1.y * cos);
                Vector u2 = new Vector(uR2.x * cos - uR2.y * sin, uR2.x * sin + uR2.y * cos);
                cV = u1;
                b.cV = u2;
            }
        } else {
            Poly p = (Poly) body;
            p.collision(body);
        }
    }

    @Override
    public void collideWithLine() {

    }

    @Override
    public void attract(Body b) {
            if (b.ball){
                double distance = c.distanceTo(b.c);
                double G = 1e4;
                Vector f = b.c.minus(c).x(G*m*b.m/Math.pow(distance, 3));
                cF.plusMe(f);
                b.cF.minusMe(f);
            }else{
                //Will be added soon
            }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(material.getColor());
        Ellipse2D.Double circle;
            circle = new Ellipse2D.Double((int)(c.x - radius), (int)(c.y - radius), (int)(2*radius), (int)(2*radius));
        g2d.fill(circle);
    }

    public void makeStatic(){
        isStatic = true;
    }
}