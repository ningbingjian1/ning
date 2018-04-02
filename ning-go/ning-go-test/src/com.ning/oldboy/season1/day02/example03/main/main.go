package main

import (
	"time"
	"fmt"
)

const (
	female = 2
	male = 1
)
const (
	b string = ""
)

func main() {
	for{
		second := time.Now().Unix()
		if (second % female == 0 ){
			fmt.Println("female")
		}else{
			fmt.Println("male")
		}
		time.Sleep(time.Millisecond * 100)
		fmt.Println(b)
	}

}

