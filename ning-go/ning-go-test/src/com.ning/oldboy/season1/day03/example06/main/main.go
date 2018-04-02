package main

import (
	"fmt"
	"math"
	"strconv"
)

//函数
//https://studygolang.com/articles/4077
func main() {
	//test1()
	//test2()
	//test3()

	//3. 变参
	//3.1 同类型
	uncert(1, 3, 5)
	uncert(2, 4)
	//3.2 不同类型
	uncert2(10.01, "Hello")
	uncert3("hello", 1)
	uncert3("hello", 1, 2, 3)
	n1 := 100
	func1(&n1)
	fmt.Println("n1 = ", n1)

	test8()

	testDefer()


	//7. panic recover
	echoA()
	echoB()
	echoC()
	/**************************/
	/*运行结果:               */
	/*It is A                 */
	/*recover: It is B        */
	/*It is C                 */
	/**************************/
}

func test9()  {

}
func testDefer() {
	fmt.Println("defer start")
	defer fmt.Println("1")
	defer fmt.Println("2")
	defer fmt.Println("3")
	fmt.Println("defer end")
}



func echoA() {
	fmt.Println("It is A")
}

func echoB() {
	defer func() {
		if err := recover(); err != nil {
			fmt.Println("recover: It is B")
		}
	}()
	//panic("It is B") //注意顺序，要在 recover 后面

	panic("----------")
}

func panic1()  {
	fmt.Println("panic---------------")
}

func echoC() {
	fmt.Println("It is C")
}



func test8()  {
	//5. 函数作为值/类型
	//匿名函数
	varFunc := testFunc
	varFunc()
	//闭包
	varFunc1 := closure(10)
	fmt.Println("varFunc1 = ",varFunc1(20)); //30
	/**************************/
	/*运行结果:               */
	/*func as a var           */
	/*30                      */
	/**************************/
}
func testFunc() {
	fmt.Println("func as a var")
}

func closure(m int) func(int) int {
	return func(n int) int {
		return m + n
	}
}
func func1(varName *int) {
//操作指针
//*varName 代替原来的varName
	*varName = 200
}
func uncert(args ...int) {
	for k, v := range args {
		fmt.Printf("%d => %d\n", k, v)
	}
	/**
	* 结果为:
	* uncert(1, 3, 5)   |   uncert(2, 4)
	* 0 => 1            |   0 => 2
	* 1 => 3            |   1 => 4
	* 2 => 5
	*/
	for k := range args {
		fmt.Printf("%d\n", k)
	}
	/**
	* 结果为: 参数(slice)的下标
	* uncert(1, 3, 5)   |   uncert(2, 4)
	* 0                 |   0
	* 1                 |   1
	* 2
	*/
	uncert2(args[1:2]) //切片
}

func uncert2(args ...interface{}) {
	for k, v := range args {
		fmt.Println(k, " => ", v)
	}
	/**
	* 结果为: 参数(slice)的下标
	* uncert2(args[1:2])
	* uncert2(3)   |   uncert2(4)
	* 0 => [3]     |   0 => [4]
	*
	* uncert2(10.01, "Hello")
	* 0 => 10.01
	* 1 => Hello
	*/
}

func uncert3(s string, n ...int) {
	for _, v := range n {
		fmt.Println(s, v)
	}
	/**
	* 结果为:
	* uncert3("hello", 1)
	* hello 1
	*
	* uncert3("hello", 1, 2, 3)
	* hello 1
	* hello 2
	* hello 3
	*/
}
func test3()  {
	fmt.Println(t1(1,2,3))
}
func t1(nums ... int) int {
	sum := 0
	for i := 0 ; i < len(nums) ;  i ++ {
		sum = sum + nums[i]
	}
	fmt.Println(sum)
	return sum
}
func test2()  {
	b := 64
	//2. 调用 math, strconv 包中的函数
	fmt.Printf("sqrt(%d) = %.2f\n", b, math.Sqrt(float64(b))) //sqrt(100) = 10.00
	c := 9
	fmt.Println("string(67) = ", string(c))                   //string(67) = C
	fmt.Println("strconv.Itoa(67) = ", strconv.Itoa(c))       //strconv.Itoa(67) = 67
	fmt.Println("strconv.Itoa(67) = ", strconv.FormatInt(int64(c),2))       //strconv.Itoa(67) = 67
}
func test1()  {
	//1. 函数的调用，多个返回值
	var (
		a = 10
		b = 100
	)
	a2, b2 := add(a, b)
	fmt.Println("a2, b2 = ", a2, ", ", b2) // a2, b2 = 20, 200
}
func add(n1, n2 int) (res1, res2 int) {
	res1, res2 = n1*2, n2*2//不需要用 ":=" 声明, 如果写为res1, res2 := n1*2, n2*2会出错
	return
}
/*
//下面的add也可以为上面的写法
func add(n1, n2 int) (res1, res2 int) {
  return n1*2, n2*2
}
*/
