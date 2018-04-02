package main

import (
	"fmt"
	"strings"
)
/**
strings 操作
https://blog.csdn.net/sanxiaxugang/article/details/60324012
 */
func main() {
	str := "   hello world you are the best   "
	fmt.Println(strings.Replace(str,"world","you",1))
}