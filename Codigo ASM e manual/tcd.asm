include<p16f873a.inc> 		; diretiva para o compilador  
cblock 0x20 			; declaração de variáveis utilizadas no programa  
 aux
endc 				; 0x20 é o início de endereço da memória de dados que 
				; o microcontrolador libera ao programador  
org 0 				; origem do programa, primeiro endereço 
goto inicio
org 4
retifie
inicio:
	banksel TXSTA 		; seta o banco do TX
	movlw b'00100110' 	; w = 00100110
	movwf TXSTA		; TXSTA = w - Configuração tx
	banksel SPBRG 		; seta o banco SPBRG
	movlw d'25'
	movwf SPBRG
	banksel RCSTA
	movlw b'10010000'
	movwf RCSTA 		; RCSTA = w = 10010000
	banksel TRISA
	movlw b'00000001'
	movwf TRISA 		; configuração do trisa = 00000001

EsperaReceber:
	banksel PIR1	
	btfss PIR1, 5 		; RCIF = 1?
	goto EsperaReceber 	; RCIF !=1
	banksel RCREG
	movf RCREG, w 		; w = dado recebido
	sublw d'1' ; w = 1 - w
	banksel STATUS 		; seta o banco do status
	btfss STATUS,Z ; z = 1?
	goto EsperaReceber 	; z != 1 ou seja w != 1

	movlw b'01000001'
	banksel ADCON0 		; seta o banco adcon
	movwf ADCON0 		; adcon = w = 01000001
	bsf ADCON0, 2 		; ativo o bit go/done
	banksel ADCON1 		; seta o banco adcon1
	movlw b'10000000'
	movwf ADCON1 		; adcon1 = w = 10000000
ReceberSinal:
	banksel ADCON0
	btfsc ADCON0, 2 	; bit go done é 0?
	goto ReceberSinal 	; bit godone = 1

; Envio do ADRESH
EsperaEnvio:
	banksel TXSTA
	btfss TXSTA, TRMT 	; TRMT = 1 ? 
	goto EsperaEnvio 	; TRMT != 1
	banksel ADRESH
	movf ADRESH,w		; w = ADRESH
	banksel TXREG
	movwf TXREG 		; TXREG = w - Envia automaticamente
; Envio do ADRESL
	movlw d'100'
	banksel aux
	movwf aux
loop:
	nop
	nop
	nop
	banksel aux
	decfsz aux, 1 		; aux = aux -1
	goto loop
	movlw d'100'
	banksel aux
	movwf aux
loop2:
	nop
	nop
	nop
	banksel aux
	decfsz aux, 1 		; aux = aux -1
	goto loop2
	movlw d'100'
	banksel aux
	movwf aux
loop3:
	nop
	nop
	nop
	banksel aux
	decfsz aux, 1 		; aux = aux -1
	goto loop3
EsperaEnvio2:
	banksel TXSTA
	btfss TXSTA, TRMT 	; TRMT = 1 ? 
	goto EsperaEnvio2 	; TRMT != 1
	banksel ADRESL
	movf ADRESL,w 		; w = ADRESL	
	banksel TXREG
	movwf TXREG 		; TXREG = w - Envia automaticamente
	movlw d'100'
	banksel aux
	movwf aux
loop4:
	nop
	nop
	nop
	banksel aux
	decfsz aux, 1 		; aux = aux -1
	goto loop4
	movlw d'100'
	banksel aux
	movwf aux
loop5:
	nop
	nop
	nop
	banksel aux
	decfsz aux, 1 		; aux = aux -1
	goto loop5
	movlw d'100' 		; w = 100
	banksel aux 		; aux = w = 100
	movwf aux
loop6:
	nop
	nop
	nop
	banksel aux
	decfsz aux, 1 		; aux = aux -1
	goto loop6
				; verifico o valor em ADRESH 
Converter:
	banksel ADRESH
	movf ADRESH, w 		; w = f

VerificaADRESL:
	banksel ADRESL
	movf ADRESL,w 		; w= ADRESL
	goto EsperaReceber


end