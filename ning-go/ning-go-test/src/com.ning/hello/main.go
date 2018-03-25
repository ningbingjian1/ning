//main 表示当前是一个可执行程序 而不是一个库
package main
//fmt是一个包名，这里表示要引入fmt这个包
import (
	"fmt"
)
func add(a int ,b int ) int {
	var sum int
	sum = a + b
	return sum
}
func main(){
	//Printf是函数
	//fmt.Printf("hello world -ning ")
	var c int
	c = add(100,200)
	fmt.Println("add(100,200) = " ,c)
}
