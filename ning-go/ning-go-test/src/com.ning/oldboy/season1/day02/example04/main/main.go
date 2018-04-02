package main

import (
	_ "os"
	"runtime"
	"fmt"
)

func main() {
	fmt.Println(runtime.GOARCH)
	fmt.Println(runtime.GOOS)
}
