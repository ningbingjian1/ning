package main

import (
	"com.ning/oldboy/season1/day02/example02/epl"
	e "com.ning/oldboy/season1/day02/example02/epl"
	"fmt"
)

const name  = "abcd"
const (
	a = 1
	b = "cc"
	d = 3
)
const (
	k = iota
	f //2
	g //3
)
func main()  {
	fmt.Println(epl.Name)
	fmt.Println(epl.Age)
	epl.Name = "IN MAIN"
	fmt.Println(epl.Name)
	fmt.Println(e.Age)
	fmt.Println(true && false)
	fmt.Println(k)
	fmt.Println(f)
	fmt.Println(g)
}
