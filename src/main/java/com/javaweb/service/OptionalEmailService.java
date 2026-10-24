package com.javaweb.service;
import org.springframework.stereotype.Service;
import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** Optional SMTP-over-TLS (port 465). Disabled unless configured via environment. */
@Service
public class OptionalEmailService {
 public void send(String to,String subject,String message){
  String host=System.getenv("LEAVEFLOW_SMTP_HOST"),username=System.getenv("LEAVEFLOW_SMTP_USER"),password=System.getenv("LEAVEFLOW_SMTP_PASS");
  String from=System.getenv("LEAVEFLOW_SMTP_FROM");
  if(host==null||host.isBlank()||username==null||password==null||from==null||to==null||!to.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))return;
  try{
   int port=Integer.parseInt(System.getenv().getOrDefault("LEAVEFLOW_SMTP_PORT","465"));
   SSLSocketFactory factory=(SSLSocketFactory)SSLSocketFactory.getDefault();
   try(SSLSocket sock=(SSLSocket)factory.createSocket()){
    sock.connect(new InetSocketAddress(host,port),4000);sock.setSoTimeout(5000);
    SSLParameters params=sock.getSSLParameters();params.setEndpointIdentificationAlgorithm("HTTPS");sock.setSSLParameters(params);sock.startHandshake();
    BufferedReader in=new BufferedReader(new InputStreamReader(sock.getInputStream(),StandardCharsets.US_ASCII));
    BufferedWriter out=new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(),StandardCharsets.US_ASCII));
    expect(in,220);command(out,in,"EHLO localhost",250);command(out,in,"AUTH LOGIN",334);
    command(out,in,Base64.getEncoder().encodeToString(username.getBytes(StandardCharsets.UTF_8)),334);
    command(out,in,Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)),235);
    command(out,in,"MAIL FROM:<"+from+">",250);command(out,in,"RCPT TO:<"+to+">",250);command(out,in,"DATA",354);
    String body=Base64.getMimeEncoder(76,"\r\n".getBytes(StandardCharsets.US_ASCII)).encodeToString(message.getBytes(StandardCharsets.UTF_8));
    String encoded="=?UTF-8?B?"+Base64.getEncoder().encodeToString(subject.getBytes(StandardCharsets.UTF_8))+"?=";
    out.write("From: <"+from+">\r\nTo: <"+to+">\r\nSubject: "+encoded+"\r\nMIME-Version: 1.0\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Transfer-Encoding: base64\r\n\r\n"+body+"\r\n.\r\n");out.flush();expect(in,250);command(out,in,"QUIT",221);
   }
  }catch(Exception ex){System.err.println("LeaveFlow email failed: "+ex.getClass().getSimpleName());}
 }
 private void command(BufferedWriter o,BufferedReader i,String cmd,int expected)throws IOException{o.write(cmd+"\r\n");o.flush();expect(i,expected);}
 private void expect(BufferedReader in,int expected)throws IOException{String line;do{line=in.readLine();if(line==null||line.length()<4)throw new IOException("SMTP disconnected");if(Integer.parseInt(line.substring(0,3))!=expected)throw new IOException("SMTP error: "+line.substring(0,3));}while(line.charAt(3)=='-');}
}
