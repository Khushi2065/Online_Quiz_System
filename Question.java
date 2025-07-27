public class Question {
    int id;
    String text, a, b, c, d, correct;

    public Question(int id, String text, String a, String b, String c, String d, String correct) {
        this.id = id;
        this.text = text;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.correct = correct;
    }
}
