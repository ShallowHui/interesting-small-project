//记得把控制台窗口大小设置为：宽110，高40
#include<stdio.h>
#include<Windows.h>
#include<stdlib.h>
#include<conio.h>
#include<time.h>

/*
	宏定义操作键
*/
#define up 'w'
#define down 's'
#define left 'a'
#define right 'd'



/*
	定义全局变量
*/
int score; //用来存放分数
struct Snake //声明一个结构体来存放蛇身的位置坐标
{
	int x; //数据域
	int y;
	struct Snake *next; //指针域
	struct Snake *last;
};
Snake snake1 = { 53,19,NULL,NULL }; //初始化蛇头作为双向链表的头节点
Snake *head = &snake1;
Snake *tail = &snake1;
struct Foods //声明一个结构体来存放食物的位置坐标
{
	int x;
	int y;
};
Foods food; //初始化一个食物
_COORD point; //声明一个结构体来存放光标的坐标
HANDLE window = GetStdHandle(STD_OUTPUT_HANDLE); //初始化一个句柄型变量用来存放控制台窗口的句柄
char clickBegin; //定义char类型变量用来存放键盘初始输入，以及有改变的键盘输入
char clickChange;



/*
	声明函数原型
*/
void dingWei(int x, int y); //声明控制光标位置的函数
void setColor(int x); //声明改变字体颜色的函数
void typeBorder(); //声明打印边界的函数
void start(); //声明打印游戏开始界面的函数
void end(); //声明打印游戏结束的界面的函数
void creatFood(); //声明在随机位置上产生食物的函数
void gameOver(); //声明打印游戏失败界面的函数
boolean eat(Snake snake); //声明判断是否吃到食物的函数
boolean eatSelf(Snake snake); //声明判断贪吃蛇是否吃到自己的函数
boolean recycle(char a); //声明判断蛇身是否逆行的函数
boolean crash(Snake snake); //声明判断蛇头是否撞到边界的函数
int changeBody(char a); //声明改变整个蛇身位置（链表）的函数
int snakeMove(char a); //声明打印蛇身的函数



/*
	主函数
*/
int main()
{
	/*
		下面四行代码用来实现隐藏光标
	*/
	CONSOLE_CURSOR_INFO io;
	GetConsoleCursorInfo(window, &io);
	io.bVisible = FALSE;
	SetConsoleCursorInfo(window, &io);
	start();
	if (_getch() == '\r') //conio.h库的函数，可以在输入一个字符（不会显示在屏幕上）后不用按回车继续往下执行程序
	{
		system("cls"); //实现清屏的函数
		typeBorder();
		dingWei(snake1.x, snake1.y);
		setColor(2);
		printf("%c", '@');
		setColor(16);
		creatFood();
		clickBegin = _getch();
		while (true)
		{
			Sleep(100); //实现程序睡眠的函数，用来控制游戏速度
			if (_kbhit()) //conio.h库的函数，检查当前键盘是否有输入，有则返回非0值，无则返回0。程序运行到kbhit()语句时程序不会暂停，而是会继续运行
			{
				clickChange = _getch();
				if (snakeMove(clickChange))
					continue;
				else
					break;
			}
			else
			{
				if (snakeMove(clickBegin))
					continue;
				else
					break;
			}
		}
		gameOver();
		end();
	}
	else
		end();
	return 0;
}



/*
	实现声明的函数
*/
void dingWei(int x, int y)
{
	point.X = x;
	point.Y = y;  
	SetConsoleCursorPosition(window, point); //移动光标到指定坐标位置
}
void setColor(int x)
{
	if (x >= 0 && x <= 15)//参数在0-15的范围内选择颜色
		SetConsoleTextAttribute(window, x);
	else //超过范围则使用默认的白色
		SetConsoleTextAttribute(window, 7);
}
void typeBorder() //定义打印边界的函数
{
	int i;
	setColor(6);
	for (i = 0; i < 110; i++) //打印上下边框
	{
		dingWei(i, 0); //调用控制光标位置的函数
		printf("%c",'*');
		dingWei(i, 39);
		printf("%c", '*');
	}
	for (i = 0; i < 40; i++) //打印左右边框
	{
		dingWei(0, i);
		printf("%c", '*');
		dingWei(109, i);
		printf("%c", '*');
	}
	setColor(16);
}
void start()
{
	setColor(3);
	dingWei(45, 12);
	printf("欢迎试玩贪吃蛇小游戏！");
	dingWei(50, 14);
	printf("规则如下：");
	dingWei(46, 15);
	printf("1.按WASD进行移动！");
	dingWei(38, 16);
	printf("2.贪吃蛇不能逆行，也不能吃到自己哦！");
	dingWei(47, 17);
	printf("3.更不能撞墙！");
	dingWei(36,22);
	printf("注意：请在英文输入法小写状态下进行试玩！");
	dingWei(37, 27);
	printf("现在按回车键开始游戏，按任意键退出程序");
	setColor(16);
}
void end()
{
	system("cls");
	dingWei(50, 15);
	printf("感谢试玩！\n");
	dingWei(0, 37);
}
void creatFood()
{
	srand(time(NULL)); //生成随机数种子
Creat:
	food.x = rand() % 108 + 1;
	food.y = rand() % 38 + 1;
	while (head != NULL) //控制食物产生的位置不会覆盖到蛇身
	{
		if (head->x == food.x && head->y == food.y)
			goto Creat; //跳出循坏重新获取食物
		else
			head = head->next;
	}
	head = &snake1; //每次做完头指针下沉的操作后要将头指针重新指回头节点
	dingWei(food.x, food.y);
	setColor(4); //调用改变字体颜色的函数
	printf("%c", '$');
	setColor(16);
}
boolean eat(Snake snake)
{
	if (snake.x == food.x && snake.y == food.y)
	{
		score++;
		creatFood();
		return true;
	}
	else
		return false;
}
boolean eatSelf(Snake snake)
{
	while (head != NULL)
	{
		if (snake.x == head->x && snake.y == head->y)
		{
			head = &snake1;
			return false;
		}
		head = head->next;
	}
	head = &snake1;
	return true;
}
boolean recycle(char a)
{
	if(clickBegin == up && clickChange == down)
		return false;
	else if (clickBegin == left && clickChange == right)
		return false;
	else if (clickBegin == down && clickChange == up)
		return false;
	else if (clickBegin == right && clickChange == left)
		return false;
	else
	{
		clickBegin = a;
		return true;
	}
}
boolean crash(Snake snake)
{
	if ((snake.x <= 0 || snake.x >= 109) || (snake.y <= 0 || snake.y >= 39))
		return false;
	else
		return true;
}
int changeBody(char a)
{
	Snake copySnake = snake1; //先创捷一个结构体来探路
	switch (a)
	{
	case up:copySnake.y--;
		break;
	case down:copySnake.y++;
		break;
	case left:copySnake.x = copySnake.x--;
		break;
	case right:copySnake.x = copySnake.x++;
		break;
	default:
		break;
	}
	if (recycle(a))
	{
		if (eatSelf(copySnake))
		{
			if (crash(copySnake))
			{
				if (eat(copySnake))
				{
					if (head->next == NULL) //以下重点实现链表的操作
					{
						Snake* snake2 = (Snake*)malloc(sizeof(Snake));
						snake2->x = snake1.x;
						snake2->y = snake1.y;
						snake2->next = NULL;
						snake2->last = &snake1;
						snake1.x = copySnake.x;
						snake1.y = copySnake.y;
						snake1.next = snake2;
						tail = snake2;
					}
					else
					{
						Snake* snake3 = (Snake*)malloc(sizeof(Snake));
						snake3->x = snake1.x;
						snake3->y = snake1.y;
						snake3->next = snake1.next;
						snake1.next->last = snake3;
						snake3->last = &snake1;
						snake1.x = copySnake.x;
						snake1.y = copySnake.y;
						snake1.next = snake3;
					}
					return 1;
				}
				else
				{
					if (head->next == NULL)
					{
						snake1.x = copySnake.x;
						snake1.y = copySnake.y;
					}
					else
					{
						head = tail;
						while (head != &snake1)
						{
							head->x = head->last->x;
							head->y = head->last->y;
							head = head->last;
						}
						head = &snake1;
						snake1.x = copySnake.x;
						snake1.y = copySnake.y;
					}
					return 1;
				}
			}
			else
				return 0;
		}
		else
			return 0;
	}
	else
		return 0;
}
int snakeMove(char a)
{
	while (head != NULL) //先擦除先前的蛇身
	{
		dingWei(head->x, head->y);
		printf(" "); //用空格覆盖
		head = head->next;
	}
	head = &snake1;
	if (changeBody(a))
	{
		while (head != NULL) //再打印改变后的蛇身
		{
			dingWei(head->x, head->y);
			setColor(2);
			printf("%c", '@');
			setColor(16);
			head = head->next;
		}
		head = &snake1;
		return 1;
	}
	else
		return 0;
}
void gameOver()
{
	system("cls");
	dingWei(46, 19);
	setColor(4);
	printf("你失败了!得分为：%d", score);
	setColor(16);
	Sleep(3000);
}
