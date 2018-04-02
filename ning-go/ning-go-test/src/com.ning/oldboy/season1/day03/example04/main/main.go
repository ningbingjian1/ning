package main

import "fmt"

/***
指针学习  https://studygolang.com/articles/108
 */

func main() {
	var a int = 1
	var b *int = &a
	var c **int = &b
	var x int = *b
	fmt.Println("a = ",a)  //1
	fmt.Println("&a = ",&a) // a 的内存地址
	fmt.Println("*&a = ",*&a) // a的内存地址上的值
	fmt.Println("b = ",b) // a的内存地址
	fmt.Println("&b = ",&b) // b的内存地址
	fmt.Println("*&b = ",*&b) // b的内存地址上的值
	fmt.Println("*b = ",*b) // b的内存地址上的值
	fmt.Println("c = ",c)
	fmt.Println("*c = ",*c)
	fmt.Println("&c = ",&c)
	fmt.Println("*&c = ",*&c)
	fmt.Println("**c = ",**c)
	fmt.Println("***&*&*&*&c = ",***&*&*&*&*&c)
	fmt.Println("x = ",x)
}