package main

import "fmt"

//[golang note] 流程控制
//https://studygolang.com/articles/6145
func main() {
	//test1()
	test2()
}
func test2()  {
	cond := 11
	switch cond {
	case 0 :
		fmt.Println(0)
	case 1 :
		fmt.Println(1)
	default:
		fmt.Println("Default")
	}
}
func test1()  {
	a1 := []int {1,2,3,4,5,6}
	for i ,v := range a1 {
		fmt.Println(i,v)
	}
}
