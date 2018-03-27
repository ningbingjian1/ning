package main

import (
	"os"
	"fmt"
	"time"
	"strings"
)

func test1()  {
	var s string
	var sep string
	//命令本身的名字
	fmt.Println(os.Args[0])
	//参数位置从1 开始
	for i := 1 ;i< len(os.Args);i++ {
		s += sep + os.Args[i]
		sep = " "
	}
	fmt.Println(s)
}
func test2()  {
	c := 0
	//while loop
	for c < 100 {
		c++
		fmt.Println(c)
	}
}
func test3()  {
	for{
		time.Sleep(time.Second * 2)
		fmt.Println(time.Now())
	}
}
//strings.Join可以提升大量字符串连接的性能
func test4()  {
	strings.Join(os.Args[1:]," ")
}
//https://books.studygolang.com/gopl-zh/ch1/ch1-02.html 课后练习1.1
func test5()  {
	fmt.Println(os.Args[0])
}
//https://books.studygolang.com/gopl-zh/ch1/ch1-02.html 课后练习1.2
func test6()  {
	for i:=0 ; i < len(os.Args); i++{
		fmt.Println("idx:" , i ,",value:" , os.Args[i])
	}
}

////https://books.studygolang.com/gopl-zh/ch1/ch1-02.html 课后练习1.3
func test7()  {
	//制造数据
	str := []string{}
	for i :=0 ; i < 100000 ;i ++ {
		str  = append(str, "abc")
	}

	//join方式
	start1 := time.Now().Nanosecond()
	s1 := strings.Join(str,"-")
	end1 := time.Now().Nanosecond()
	fmt.Println("耗时1:",(end1 - start1), "纳秒",len(s1))



	//拼接方式
	sep := "-"
	s2 := ""
	start2 := time.Now().Nanosecond()
	for i:= 0 ;i < len(str) ; i ++{
		if(i == 0 ){
			s2 += str[i]
		}else{
			s2 += sep + str[i]
		}

	}
	end2 := time.Now().Nanosecond()
	fmt.Println(end2)
	fmt.Println(start2)
	fmt.Println("耗时2:",(end2 - start2), "纳秒",len(s2))
}

func main()  {
	//test2()
	//test3()
	//test4()
	//test6()
	test7()
}
