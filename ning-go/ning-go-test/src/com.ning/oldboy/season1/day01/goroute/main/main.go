package main
//go build -o bin/goroute.exe  com.ning\oldboy\season1\day1\goroute\main
import (
	"com.ning/oldboy/season1/day01/goroute"
	"fmt"
)
func main() {
	var pipe chan int
	pipe = make(chan int ,1)
	go goroute.Add(100,200,pipe)
	sum := <- pipe
	fmt.Println("sum = " , sum )
}
