
add $r0, $r0, $sp # r0 = 256
add $r1, $r1, $sp # r1 = 256
muli $r0, $r0, 8 # r1 = 256*8
j cond                  # goto loop condition
loop:
    muli $r1, $r1, 2
cond:
    slt $r2, $r1, $r0
    bne $r2, $r3, loop

syscall $r0
syscall $r1
