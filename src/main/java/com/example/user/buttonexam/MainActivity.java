package com.example.user.buttonexam;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

class LineView extends View{

    public int cc =0;
    public float pos[][]=new float [4][2]; //좌표저장용
    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas){

        Paint paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        Path path = new Path();

        path.moveTo(pos[0][0],pos[0][1]);
        //path.lineTo(pos[0][0],pos[0][1]);
        //path.lineTo(pos[cc][0],pos[cc][1]);
        for(int i=0;i<=cc;i++) path.lineTo(pos[i][0], pos[i][1]);

        canvas.drawPath(path,paint);

    }
}
public class MainActivity extends Activity {
    public static final int N=10;
    Button btn[][] = null;
    GridLayout g;
    boolean buttonCheck = false;
    boolean mCheck=false;
    int map_alive[]={60,60,44}; //60개버튼
    int alive; //버튼 남아있는갯수
    int first_row, first_col;
    int cnt=0;
    int dr[] = {0, -1, 0, 1}, dc[] = {1, 0, -1, 0};//각각 direction=0,1,2,3 = 오른쪽 위 왼쪽 아래
    int cc[][] = new int[4][2];//임시 배열
    int corner[][]=new int [4][2];
    int[] glo;

    char map[][];
    char mapset[][][]=
            {{"000000000000".toCharArray(),
             "011111111110".toCharArray(),
             "010000000010".toCharArray(),
             "010111111010".toCharArray(),
             "010100001010".toCharArray(),
             "010101101010".toCharArray(),
             "010101101010".toCharArray(),
             "010100001010".toCharArray(),
             "010111111010".toCharArray(),
             "010000000010".toCharArray(),
             "011111111110".toCharArray(),
             "000000000000".toCharArray(),},

             {"000000000000".toCharArray(),
              "011111111110".toCharArray(),
              "010000000010".toCharArray(),
              "011111111110".toCharArray(),
              "000000000000".toCharArray(),
              "001111111100".toCharArray(),
              "001111111100".toCharArray(),
              "000000000000".toCharArray(),
              "011111111110".toCharArray(),
              "010000000010".toCharArray(),
              "011111111110".toCharArray(),
              "000000000000".toCharArray()},

             {"000000000000".toCharArray(),
              "000100010000".toCharArray(),
              "001000111000".toCharArray(),
              "011001111100".toCharArray(),
              "011100001100".toCharArray(),
              "011100000110".toCharArray(),
              "011110000010".toCharArray(),
              "001100001000".toCharArray(),
              "000000011100".toCharArray(),
              "001101111100".toCharArray(),
              "000011111000".toCharArray(),
              "000001110000".toCharArray(),
              "000000000000".toCharArray()}};

    TextView t,te;

    public void dfs(int sr, int sc, int er, int ec, int ccnt, int direction, int count) // ccnt=꺾이는 횟수 카운트 direction=방향 count=거리
            //map 배열이 1~10까지 세기때문에 1칸 늘려서 셈
    {
        if (ccnt > 3) return; // 3번 이상 꺾임 -> 처음엔 direction이 -1이기때문에 무조건 1번 세기 때문
        if (sc == ec && sr == er) //도착
        {
            LineView l=(LineView)findViewById(R.id.line);
            if(l.cc >ccnt || cnt>count) //최소경로발견
            {
                l.cc = ccnt;
                cnt = count;
                for (int i = 0; i < 4; i++) corner[i][0] = corner[i][1] = 0; //초기화
                for (int i = 0; i < ccnt; i++) {
                    corner[i][0] = cc[i][0];
                    corner[i][1] = cc[i][1];
                }
                corner[ccnt][0]=er-1;
                corner[ccnt][1]=ec-1;
            }
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (sr + dr[i] < 0 || sr + dr[i] >= N+1 || sc + dc[i] < 0 || sc + dc[i] >= N+1 || (!(sr+dr[i]==er && sc+dc[i]==ec) && map[sr+dr[i]][sc+dc[i]]=='1'))
                continue; //칸 빠져나감
            if (i != direction) //방향이 바뀜
            {
                cc[ccnt][0] = sr-1;
                cc[ccnt][1] = sc-1; //코너 좌표 저장
                dfs(sr + dr[i], sc + dc[i], er, ec, ccnt + 1, i,count+1);
            } else dfs(sr + dr[i], sc + dc[i], er, ec, ccnt, direction,count+1);
        }
    }

    public void ClickButton(final int row, final int col) {
        //빠르게 버튼을 없애면 안없어지는 현상이 일어남;
        mCheck=false;
        final LineView l=(LineView)findViewById(R.id.line);



        //버튼 가로세로 65,85
        //gridview 위치 33,258
        //Toast to = Toast.makeText(this, glo[0]+","+glo[1] , Toast.LENGTH_SHORT); to.show();

        if (buttonCheck) { //버튼이 몇개 눌렸는지 체크하는변수
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.parseColor("#000000")); // 검은색으로 바꿈
            gd.setCornerRadius(5);
            btn[row][col].setTextColor(Color.parseColor("#FFFFFF"));
            btn[row][col].setBackgroundDrawable(gd);


            //dfs(first_row, first_col, row, col, 0, -1);
            if(btn[row][col].getText()!=btn[first_row][first_col].getText()) mCheck=false; //무늬가 다르면 dfs 실행X
            else
            {
                l.cc =N;
                cnt=99;
                dfs(first_row+1,first_col+1,row+1,col+1,0,-1,0);
                if(l.cc !=N)
                {
                    te.setText("");
                    mCheck=true;
                    int [] location=new int [2];
                    btn[corner[0][0]][corner[0][1]].getLocationInWindow(location);

                    l.pos[0][0]=(float)(location[0]+32.5);
                    l.pos[0][1]=(float)(location[1]-172+42.5+4.5); //x,y값 0으로 받아오는

                    //코너 처리
                    for(int ii=1;ii<=l.cc;ii++)
                    {
                        l.pos[ii][0]=(float)(l.pos[ii-1][0]+(65*(corner[ii][1]-corner[ii-1][1])));
                        l.pos[ii][1]=(float)(l.pos[ii-1][1]+(85*(corner[ii][0]-corner[ii-1][0]))); //corner는 row-col식이고 좌표는 x-y식이다
                    }

                    /*btn[corner[l.cc][0]][corner[l.cc][1]].getLocationInWindow(location);
                    l.pos[l.cc][0]=(float)(location[0]+32.5);
                    l.pos[l.cc][1]=(float)(location[1]-172+42.5);*/

                    te.setText(" ("+l.pos[0][0]+","+l.pos[0][1]+")("+l.pos[1][0]+","+l.pos[1][1]+")("+l.pos[2][0]+","+l.pos[2][1]+")("+l.pos[3][0]+","+l.pos[3][1]+")\n"+
                    "("+corner[0][0]+","+corner[0][1]+")("+corner[1][0]+","+corner[1][1]+")("+corner[2][0]+","+corner[2][1]+")("+corner[3][0]+","+corner[3][1]+")");
                    l.invalidate();
                }
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final GradientDrawable gdd = new GradientDrawable();
                    if (mCheck) {
                        //l.invalidate();
                        gdd.setColor(Color.parseColor("#FFFFFF")); // 흰색으로 바꿈
                        gdd.setCornerRadius(5);
                        gdd.setStroke(1, Color.parseColor("#000000"));

                        btn[first_row][first_col].setTextColor(Color.parseColor("#000000"));
                        btn[first_row][first_col].setBackgroundDrawable(gdd);
                        btn[row][col].setTextColor(Color.parseColor("#000000"));
                        btn[row][col].setBackgroundDrawable(gdd);

                        btn[first_row][first_col].setVisibility(Button.INVISIBLE);
                        btn[row][col].setVisibility(Button.INVISIBLE);

                        map[row+1][col+1]='0';
                        map[first_row+1][first_col+1]='0'; //map에서도 1(블록)->0(빈칸)으로 바꿔줌

                        l.pos[0][0]=l.pos[0][1];
                        l.cc =0;
                        l.invalidate();
                    }//안보이게함
                    else {
                        gdd.setColor(Color.parseColor("#FFFFFF")); // 흰색으로 바꿈
                        gdd.setCornerRadius(5);
                        gdd.setStroke(1, Color.parseColor("#000000"));

                        btn[first_row][first_col].setTextColor(Color.parseColor("#000000"));
                        btn[first_row][first_col].setBackgroundDrawable(gdd);
                        btn[row][col].setTextColor(Color.parseColor("#000000"));
                        btn[row][col].setBackgroundDrawable(gdd);
                    }
                }
            }, 250); //둘다 색 바꾸고 0.25초 뒤에 흰색으로 다시 돌아옴

            if(mCheck) alive-=2;

            String ts="";

            btn[first_row][first_col].setEnabled(true); //다시 버튼 활성화
            /*if(mCheck)
                for(int i=0;i<cc;i++) ts=ts+pos[i][0]+" "+pos[i][1]+"->";

            ts=ts+row+" "+col;
            ts+="<"+mCheck+">";
            Toast toast = Toast.makeText(this, ts, Toast.LENGTH_LONG);
            toast.show();
            toast=Toast.makeText(this,Float.toString(btn[pos[0][0]][pos[0][1]].getX()),Toast.LENGTH_SHORT);
            toast.show();*/
        } else {
            first_row = row;
            first_col = col; //먼저 눌린 버튼 위치 저장

            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.parseColor("#000000")); // 검은색으로 바꿈
            gd.setCornerRadius(5);
            btn[row][col].setBackgroundDrawable(gd);
            btn[row][col].setTextColor(Color.parseColor("#FFFFFF"));
            btn[row][col].setEnabled(false); //같은 버튼 또 눌리는 일 방지 -> 비활성화

        }

        buttonCheck = !buttonCheck; //버튼이 1개 눌림 = f, 버튼이 2개 눌림 = t

        if(alive==0)
        {
            Toast toast = Toast.makeText(this, "game over" , Toast.LENGTH_SHORT);
            toast.show();
            //gameover();
        }

        t.setText(Integer.toString(alive));
    }
    public void buttonCreate(LinearLayout button_layout, GridLayout grid) {
        int pair = 15;
        int check[] = new int[pair]; //pair*4짝
        button_layout.removeView(grid); //레이아웃 초기화
        grid.removeAllViews();   //버튼(들어있는 레이아웃) 초기화

        grid.setRowCount(N);
        grid.setColumnCount(N);


        btn = new Button[N][]; //동적 버튼배열
        for (int i = 0; i < N; i++) {
            btn[i] = new Button[N];
            for (int j = 0; j < N; j++) {
                Random random = new Random();
                btn[i][j] = new Button(MainActivity.this);
                if(map[i+1][j+1]=='0') continue;
                for (; ; ) {
                    int rand = random.nextInt(pair); //0~3 랜덤
                    if (check[rand] < 4) {
                        check[rand]++;
                        btn[i][j].setText(Integer.toString(rand + 1));
                        break;
                    }
                }

                int a = i * btn[i].length + j + 1;
                String text = String.format("%02d", a);
                btn[i][j].setTextSize(10);
                btn[i][j].setId(i);
                final int r = i;
                final int c = j;
                btn[i][j].setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View v) {
                                ClickButton(r, c);
                            }
                        }
                );

                GridLayout.Spec row = GridLayout.spec(i, 1); //i행
                GridLayout.Spec col = GridLayout.spec(j, 1); //j열
                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(row, col);
                gridParam.width = 65;
                gridParam.height = 85; //한 칸의 크기
                gridParam.setMargins(0,2,2,2);

                GradientDrawable gd = new GradientDrawable();
                gd.setColor(Color.parseColor("#FFFFFF")); // Changes this drawbale to use a single color instead of a gradient
                gd.setCornerRadius(5);
                gd.setStroke(1, Color.parseColor("#000000"));
                btn[i][j].setBackgroundDrawable(gd); //버튼의 실제 크기가 달라서 여백이 있는것처럼 보이지만 사실은 아님 */

                grid.addView(btn[i][j], gridParam);
                if(map[i+1][j+1]=='0') btn[i][j].setVisibility(Button.INVISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //map
        map=new char [N+2][N+2];
        for(int i=0;i<N+2;i++)
            for(int j=0;j<N+2;j++)
                map[i][j]= mapset[0][i][j];

        //map 배열 0(공간), 1~10(유효버튼), 11(공간)
        alive=map_alive[0];

        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout); //전체 레이아웃

        g = new GridLayout(MainActivity.this);
        g.setOrientation(GridLayout.HORIZONTAL);
        g.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, GridView.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER);


        LinearLayout pl = new LinearLayout(this); // pl -> debug용 위젯 들어있는 레이아웃

        Button reset = new Button(this);
        reset.setText("RESET");
        pl.addView(reset);

        Button undo = new Button(this);
        undo.setText("UNDO");
        //pl.addView(undo);

        t=new TextView(this);
        t.setText(Integer.toString(alive));
        pl.addView(t);

        te=new TextView(this);
        te.setText("");
        te.setTextSize(7);
        pl.addView(te);

        layout.addView(pl);

        reset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                 map=new char [N+2][N+2];
                 for(int i=0;i<N+2;i++)
                     for(int j=0;j<N+2;j++)
                         map[i][j]= mapset[0][i][j];

                for(int i=0;i<N;i++)
                    for(int j=0;j<N;j++) {
                        if (map[i + 1][j + 1] == '0') continue;
                        btn[i][j].setVisibility(Button.VISIBLE);
                    }
                 alive=map_alive[0];
            }
        });
        undo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                //undo;
            }
        });


        /*final TextView debugView=new TextView(this);

        pl.addView(debugView);
        layout.addView(pl);



        ok.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v)
                    {
                        buttonCreate(layout,g); //전체 레이아웃, 레이아웃, 행,열
                        layout.addView(g);
                    }
                });
        */

        buttonCreate(layout, g);
        layout.addView(g);

    }
}
