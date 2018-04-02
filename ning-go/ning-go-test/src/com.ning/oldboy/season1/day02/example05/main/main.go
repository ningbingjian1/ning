package main

import (
	"fmt"
	"math/rand"
	//"time"
	//"time"
	"strconv"
)

func main() {
	//test1()
	//test2()
	//test3()
	//test4()
	//test5()
	//test6()
	test7()
}
//0 - 100 质数
func test1(){
	for i:= 0; i < 100; i ++ {
		isPrime := true
		for j := 2; j < i  ; j ++  {
			if (i % j == 0) {
				isPrime = false
				continue
			}
		}
		if isPrime {
			fmt.Println("i = " ,i)
		}

	}
}

//随机数生成
func test2()  {
	/*r := rand.New(rand.NewSource(time.Now().UnixNano()))
	for i:=0; i<10; i++ {
		fmt.Println(r.Intn(100))
	}*/

	//rand.Seed(int64(time.Now().Nanosecond()))
	rand.Seed(1000)
	for i:=0; i<10; i++ {
		fmt.Println(rand.Intn(100))
	}
}
//字符串翻转
func test3()  {
	str1 := "hello world"
	var str2 string
	len := len(str1)
	for i := 0 ; i < len ; i ++  {
		str2 = str2 + fmt.Sprintf("%c",str1[len - 1 - i ])
	}
	fmt.Println(str2)
}
//字符串翻转
func test4()  {
	var strResult[]byte
	str1 := "hello world"
	bytes := []byte(str1)
	len := len(str1)
	for i := 1 ; i < len ; i++ {
		strResult = append(strResult,bytes[i])
	}
	fmt.Println(string(strResult))
}

//求n得阶乘
func test5()  {
	n := 4
	sum := 0
	now := 1
	total := 0
	for i := 1 ; i <= n ; i ++ {
		now = now * i
		sum = sum + now
		total = total + sum
		fmt.Printf("%d,%d,%d,%d \n", i ,now ,sum,total)
	}
	fmt.Println(sum)
}
func nul(n int )  int {
	if(n == 0 || n == 1){
		return 1
	}
	return n * nul(n - 1)
}

/*
打印出100-999中所有的“水仙花数”，所谓“水仙花数”是指一个三位数，其各位数字
立方和等于该数本身。例如：153 是一个“水仙花数”，因为 153=1 的三次
方＋5 的三次方＋3 的三次方。
*/

func test6()  {
	for i := 100; i< 999 ; i ++  {
		a1 := i / 100
		a2 := i % 100 / 10
		a3 := i % 100 % 10 % 10
		a4 := a1 * a1 * a1 + a2 * a2 * a2 + a3 * a3 * a3
		if (a4 == i ) {
			fmt.Println(i,a1,a2,a3)
		}

	}
	
}
//控制台输入
func test7()  {
	var n int
	fmt.Scanf("%d",&n)
	fmt.Println("您输入的是:" ,n)

	fmt.Println(&n);
	//函数传的是值引用 函数返回后,不会修改原来的n1
	var n1 int
	fmt.Scanf("请再次输入%d:",n1)
	fmt.Println("第二次输入的是:",n1)

	i1 ,error := strconv.Atoi("aa")
	if error == nil {
		fmt.Println(i1)
	}else {
		fmt.Println("发生错误:",error)
	}

}
