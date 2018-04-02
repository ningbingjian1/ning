package main
//go build  com.ning\oldboy\season1\day1\pkg\main
import (
	//引入自定义的包
	"com.ning/oldboy/season1/day01/pkg/module1"
	"fmt"
)
func main() {
	sum := module1.Add(1 ,2)
	fmt.Println("sum = ===" , sum)
}
