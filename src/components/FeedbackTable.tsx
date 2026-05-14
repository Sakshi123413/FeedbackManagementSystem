import { useState, useEffect, useCallback } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Spinner } from "@/components/ui/spinner"
import { getFeedbacks, type Feedback } from "@/services/feedbackService"
import { Search, Star, MessageSquare } from "lucide-react"
import { cn } from "@/lib/utils"

interface FeedbackTableProps {
  refreshKey: number
}

export function FeedbackTable({ refreshKey }: FeedbackTableProps) {
  const [feedbacks, setFeedbacks] = useState<Feedback[]>([])
  const [search, setSearch] = useState("")
  const [isLoading, setIsLoading] = useState(true)

  const loadFeedbacks = useCallback(async () => {
    setIsLoading(true)
    try {
      const data = await getFeedbacks()
      setFeedbacks(data)
    } finally {
      setIsLoading(false)
    }
  }, [])

  useEffect(() => {
    loadFeedbacks()
  }, [loadFeedbacks, refreshKey])

  const filtered = feedbacks.filter(
    (f) =>
      f.userName.toLowerCase().includes(search.toLowerCase()) ||
      f.title.toLowerCase().includes(search.toLowerCase()) ||
      f.message.toLowerCase().includes(search.toLowerCase())
  )

  return (
    <Card>
      <CardHeader>
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <CardTitle className="text-lg">All Feedback</CardTitle>
          <div className="w-full sm:w-64">
            <Input
              placeholder="Search feedback..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              icon={<Search className="h-4 w-4" />}
            />
          </div>
        </div>
      </CardHeader>
      <CardContent>
        {isLoading ? (
          <div className="py-12">
            <Spinner />
          </div>
        ) : filtered.length === 0 ? (
          <EmptyState hasSearch={search.length > 0} />
        ) : (
          <>
            {/* Desktop Table */}
            <div className="hidden md:block overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b">
                    <th className="text-left text-xs font-medium text-muted-foreground uppercase tracking-wider py-3 px-4">
                      User
                    </th>
                    <th className="text-left text-xs font-medium text-muted-foreground uppercase tracking-wider py-3 px-4">
                      Feedback
                    </th>
                    <th className="text-left text-xs font-medium text-muted-foreground uppercase tracking-wider py-3 px-4">
                      Rating
                    </th>
                    <th className="text-left text-xs font-medium text-muted-foreground uppercase tracking-wider py-3 px-4">
                      Date
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map((feedback) => (
                    <tr
                      key={feedback.id}
                      className="border-b last:border-0 hover:bg-accent/50 transition-smooth"
                    >
                      <td className="py-4 px-4">
                        <div className="flex items-center gap-3">
                          <div className="h-8 w-8 rounded-full gradient-primary flex items-center justify-center text-xs font-medium text-primary-foreground">
                            {feedback.userName.charAt(0).toUpperCase()}
                          </div>
                          <span className="font-medium text-sm text-foreground">
                            {feedback.userName}
                          </span>
                        </div>
                      </td>
                      <td className="py-4 px-4">
                        <p className="text-sm font-medium text-foreground">{feedback.title}</p>
                        <p className="text-xs text-muted-foreground mt-0.5 line-clamp-1">
                          {feedback.message}
                        </p>
                      </td>
                      <td className="py-4 px-4">
                        <RatingStars rating={feedback.rating} />
                      </td>
                      <td className="py-4 px-4">
                        <span className="text-sm text-muted-foreground">
                          {formatDate(feedback.date)}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* Mobile Cards */}
            <div className="md:hidden space-y-3">
              {filtered.map((feedback) => (
                <div
                  key={feedback.id}
                  className="p-4 rounded-xl border bg-surface transition-smooth hover:shadow-card"
                >
                  <div className="flex items-center justify-between mb-2">
                    <div className="flex items-center gap-2">
                      <div className="h-7 w-7 rounded-full gradient-primary flex items-center justify-center text-xs font-medium text-primary-foreground">
                        {feedback.userName.charAt(0).toUpperCase()}
                      </div>
                      <span className="text-sm font-medium text-foreground">
                        {feedback.userName}
                      </span>
                    </div>
                    <RatingStars rating={feedback.rating} size="sm" />
                  </div>
                  <p className="text-sm font-medium text-foreground">{feedback.title}</p>
                  <p className="text-xs text-muted-foreground mt-1 line-clamp-2">{feedback.message}</p>
                  <p className="text-xs text-muted-foreground mt-2">{formatDate(feedback.date)}</p>
                </div>
              ))}
            </div>
          </>
        )}
      </CardContent>
    </Card>
  )
}

function RatingStars({ rating, size = "md" }: { rating: number; size?: "sm" | "md" }) {
  return (
    <div className="flex gap-0.5">
      {Array.from({ length: 5 }, (_, i) => (
        <Star
          key={i}
          className={cn(
            size === "sm" ? "h-3.5 w-3.5" : "h-4 w-4",
            i < rating ? "fill-warning text-warning" : "fill-transparent text-muted-foreground/30"
          )}
        />
      ))}
    </div>
  )
}

function EmptyState({ hasSearch }: { hasSearch: boolean }) {
  return (
    <div className="flex flex-col items-center justify-center py-16 text-center">
      <div className="h-16 w-16 rounded-2xl bg-accent flex items-center justify-center mb-4">
        {hasSearch ? (
          <Search className="h-7 w-7 text-muted-foreground" />
        ) : (
          <MessageSquare className="h-7 w-7 text-muted-foreground" />
        )}
      </div>
      <h3 className="text-lg font-semibold text-foreground mb-1">
        {hasSearch ? "No results found" : "No feedback yet"}
      </h3>
      <p className="text-sm text-muted-foreground max-w-sm">
        {hasSearch
          ? "Try adjusting your search terms to find what you're looking for."
          : "Be the first to share your feedback. Your input helps us improve."}
      </p>
    </div>
  )
}

function formatDate(dateStr: string): string {
  const date = new Date(dateStr)
  return date.toLocaleDateString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
  })
}

