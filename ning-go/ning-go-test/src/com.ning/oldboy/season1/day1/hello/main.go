//main 表示当前是一个可执行程序 而不是一个库
package main

import (
	"fmt"
	//"time"
	"time"
)

//fmt是一个包名，这里表示要引入fmt这个包

//定义一个全局的管道
var pipe chan int

func add(a int, b int)  {
	sum := a + b
	time.Sleep(time.Second * 3)
	pipe <- sum
	return
}

//多返回值
func cal(a int,b int )(int ,int ){
	return a + b ,(a + b ) / 2
}
func main() {
	pipe = make(chan int,1)
	go add(1,2)
	//这里如果没有数据 会一直阻塞 直到管道有数据
	sum1 :=<- pipe
	fmt.Println("sum1 = " , sum1 )
	go add(3,4)
	sum2 :=<- pipe
	fmt.Println("sum2 = " , sum2)
	sum ,avg := cal(100,200)
	fmt.Println("sum = " ,sum,",avg = " ,avg)
	sum3 ,_ := cal(100,200)
	fmt.Println("sum3 = " ,sum3 ,",avg = " )
	//time.Sleep(time.Second * 10)
}
