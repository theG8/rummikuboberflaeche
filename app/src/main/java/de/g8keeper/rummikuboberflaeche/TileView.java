package de.g8keeper.rummikuboberflaeche;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Tile;






public class TileView extends View {

    private static final String TAG = TileView.class.getSimpleName();

    private Context context;
    private Paint paint = new Paint();


    private Tile mTile = null;



    public TileView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;

        setOnLongClickListener(new OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {

//
//                Intent intent = new Intent();
//                intent.putExtra("isFromTileSet",)

                ClipData data = ClipData.newPlainText(v.getParent().toString(),""); //this.toString());

                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data,dragShadowBuilder,v,0);

                return true;
            }
        });



        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        paint.setTextAlign(Paint.Align.CENTER);

    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int backgroundColor = getResources().getColor(R.color.tile_background, null);
        int textColor;
        String valueText;



        paint.setColor(backgroundColor);
        canvas.drawRect(0, 0, getRight(), getBottom(), paint);


        if (mTile != null) {

            if (mTile.isJoker()){
                textColor = ContextCompat.getColor(getContext(), R.color.tile_joker);
                valueText = "J";
            } else {
                textColor = ContextCompat.getColor(getContext(), mTile.getColor().colorId());
                valueText = Integer.toString(mTile.getValue());
            }

        } else {
            valueText = Integer.toString(Tile.MAX_VALUE);
            textColor = ContextCompat.getColor(getContext(), Color.RED.colorId());
        }


//        Log.d(TAG, "onDraw: valueText: " + valueText + " textColor: " + textColor);

        int i = 1;

        paint.setTextSize(i);

        while (paint.measureText(Integer.toString(Tile.MAX_VALUE)) < (getWidth() /3*2)) {
            paint.setTextSize(++i);
        }

        paint.setColor(textColor);
        canvas.drawText(valueText, getWidth() / 2, getHeight() / 2, paint);

    }

    @Override
    public String toString() {
        return mTile.toString();
    }



    public static TileView newInstance(Context context, Tile tile){
        int width = context.getResources().getDimensionPixelSize(R.dimen.tile_width);

        return newInstance(context,tile,width);
    }

    public static TileView newInstance(Context context, Tile tile, int width){
        TileView tv = new TileView(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                width,
                (int) (width * 1.4)
        );



        int margin = context.getResources().getDimensionPixelSize(R.dimen.tile_margin);
        params.setMargins(margin,margin,margin,margin);

        tv.setLayoutParams(params);

        tv.setTile(tile);

        return tv;
    }

    public Tile getTile() {
        return mTile;
    }

    private void setTile(Tile tile) {
        this.mTile = tile;
        postInvalidate();
    }

    //*********************************************************************************************



}
