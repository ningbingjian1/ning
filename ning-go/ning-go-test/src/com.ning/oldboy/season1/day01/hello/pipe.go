package main

import "fmt"
import "time"
func m1(){
	for i := 0;i< 100;i++ {
		go fmt.Println(i)
	}
	time.Sleep(time.Second * 10)
}
func m2(){
	pipe := make(chan int ,3)

	pipe <- 1
	pipe <- 2
	pipe <- 3
	pipe <- 4
	//fmt.Println(len(pipe))

	var t1 int
	t1 =<- pipe
	fmt.Println(t1)
	t1 =<- pipe
	fmt.Println(t1)
	t1 =<- pipe
	fmt.Println(t1)

}

