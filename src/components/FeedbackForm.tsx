import { useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Button } from "@/components/ui/button"
import { RatingInput } from "@/components/ui/rating-input"
import { useToast } from "@/components/ui/toast"
import { submitFeedback } from "@/services/feedbackService"
import { Send } from "lucide-react"

interface FeedbackFormProps {
  onSubmitted: () => void
}

export function FeedbackForm({ onSubmitted }: FeedbackFormProps) {
  const [title, setTitle] = useState("")
  const [message, setMessage] = useState("")
  const [rating, setRating] = useState(0)
  const [errors, setErrors] = useState<Record<string, string>>({})
  const [isLoading, setIsLoading] = useState(false)
  const { showToast } = useToast()

  const validate = () => {
    const newErrors: Record<string, string> = {}
    if (!title.trim()) newErrors.title = "Title is required"
    if (!message.trim()) newErrors.message = "Message is required"
    if (rating === 0) newErrors.rating = "Please select a rating"
    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!validate()) return

    setIsLoading(true)
    try {
      await submitFeedback({
        title,
        message,
        rating,
      })
      showToast("Feedback submitted successfully!", "success")
      setTitle("")
      setMessage("")
      setRating(0)
      setErrors({})
      onSubmitted()
    } catch {
      showToast("Failed to submit feedback. Please try again.", "error")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-lg">Submit Feedback</CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Title"
            placeholder="Brief summary of your feedback"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            error={errors.title}
          />
          <Textarea
            label="Message"
            placeholder="Share your detailed feedback here..."
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            error={errors.message}
          />
          <RatingInput
            label="Rating"
            value={rating}
            onChange={setRating}
            error={errors.rating}
          />
          <Button type="submit" className="w-full" isLoading={isLoading}>
            <Send className="h-4 w-4" />
            Submit Feedback
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
