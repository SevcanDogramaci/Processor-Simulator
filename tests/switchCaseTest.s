addi $t1, $t0, 1
addi $t4, $t0, 1            # set temp to 1

bne $t1, $t4, C2_COND       # case 1 false : branch to case 2 condition
j C1_BODY                   # case 1 true : branch to case 1 body imp.

C2_COND: addi $t4, $t0, 2   # set temp to 2
bne $t1, $t4, C3_COND       # case 2 false : branch to case 3 condition
j C2_BODY                   # case 2 true : branch to case 2 body imp.

C3_COND: addi $t4, $t0, 3   # set temp to 3
bne $t1, $t4, EXIT          # case 3 false : exit
j C3_BODY                   # case 3 true : branch to case 3 body imp.

C1_BODY: addi $t1, $t1, 1   # case 1 body: i++
j EXIT                      # break
C2_BODY: addi $t1, $t1, 2   # case 2 body: i += 2
j EXIT                      # break
C3_BODY: addi $t1, $t1, 3   # case 3 body: i += 3
EXIT: